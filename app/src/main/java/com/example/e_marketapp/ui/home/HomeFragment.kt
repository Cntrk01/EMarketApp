package com.example.e_marketapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.HomeAdapter
import com.example.e_marketapp.databinding.FragmentHomeBinding
import com.example.e_marketapp.local.MarketBasketEntity
import com.example.e_marketapp.local.MarketEntity
import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import com.example.e_marketapp.viewmodel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val marketViewModel: MarketViewModel by viewModels()
    private val marketDbViewModel: MarketDbViewModel by viewModels()
    private var marketSize = 4
    private var marketList = ArrayList<BaseModelItem>()

    private var checkScrollLastPosition = true
    private var scrollingUp = true
    private var newCheckState = 1
    private lateinit var homeAdapter: HomeAdapter
    private var isFirstLoad=true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMarketData()
        searchMarketData()
        initAdapter()
    }

    private fun observeMarketData() {
        marketViewModel.viewModelScope.launch {
            marketViewModel.marketDataState.collectLatest {
                binding.apply {
                    if (it.isLoading) {
                        homeProgressBar.visibility = View.VISIBLE
                    } else if (it.error.toString().isNotEmpty()) {
                        homeErrorText.visibility = View.VISIBLE
                        homeProgressBar.visibility = View.GONE
                        homeErrorText.text = it.error.toString()
                    } else {
                        homeErrorText.visibility = View.GONE
                        homeProgressBar.visibility = View.GONE
                        homeRecyclerView.visibility = View.VISIBLE
                        it.marketModel?.let { newMarketData ->
                            if (isFirstLoad || scrollingUp) {
                                marketList.clear()
                                marketList.addAll(newMarketData)
                                homeAdapter.addData(marketList.take(marketSize))
                                isFirstLoad = false
                            }
                        }
                    }
                }
            }
        }
        marketDbViewModel.viewModelScope.launch {
            marketDbViewModel.getAllData.collectLatest {
                if (it.marketData?.isNotEmpty() == true) {
                    if (it.isDeleted != true) {
                        homeAdapter.checkList(checkList = it.marketData as ArrayList<MarketEntity>)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        homeAdapter = HomeAdapter(clickItemData = {
            val action = HomeFragmentDirections.actionHomeToDetailFragment(itemArgs = it)
            findNavController().navigate(action)
        }, clickFavorite = { it, checkRoomDb ->
            if (checkRoomDb) {
                marketDbViewModel.addMarketItem(
                    marketItem = MarketEntity(
                        marketId = it.id,
                        brand = it.brand,
                        createdAt = it.createdAt,
                        description = it.description,
                        image = it.image,
                        model = it.model,
                        name = it.name,
                        price = it.price
                    )
                )
            } else {
                marketDbViewModel.deleteMarketItem(it.id)
            }
        }, addToCardClick = {
            marketDbViewModel.addBasketItem(
                MarketBasketEntity(
                    productId = it.id,
                    productCount = 1,
                    productName = it.name,
                    productPrice = it.price.toDouble(),
                    singleItemPrice = it.price
                )
            )
        })
        homeAdapter.notifyDataSetChanged()
        binding.homeRecyclerView.adapter = homeAdapter
        binding.homeRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.homeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //bunu yazma amacım ise kullanıcı geri başa doğru kaydırdıgında listeye eklemenin önüne geçmekti
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    scrollingUp = true
                } else if (dy < 0) {
                    scrollingUp = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState % newCheckState == 0 && scrollingUp) {
                    newCheckState += 1
                    //Bu kontrolü saglama sebebim kullanıcı en sona geldiğinde kaydırmaya devam ederse ekleme işlemini yapıyor.Bundan dolayı son size = olunca artık çalışmayacak
                    //Sadece liste içinde değil api olsaydı sürekli istek atarak api isteğini şişiricekti önemli bir kontrol !
                    if (marketSize != marketList.size) {
                        if (marketSize > marketList.size) {
                            marketSize = marketList.size
                            checkScrollLastPosition = false
                        }
                        //bunu koyma sebebimde değişken değeri scroll denedikçe artmaya devam etmesi
                        if (checkScrollLastPosition) {
                            marketSize += 4
                            //Bu fonksiyonun içinde println var aşağı kaydırdıkça eklediğini logcatta gözlemleyebilirsiniz.
                            homeAdapter.addData(marketList.take(marketSize))
                        }
                    }
                }
            }
        })
    }

    private fun searchMarketData() {
        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                @SuppressLint("SetTextI18n")
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    scrollingUp = false
                    val filteredList =
                        marketList.filter { it.name.equals(query, ignoreCase = true) }

                    if (filteredList.isEmpty()) {
                        homeRecyclerView.visibility = View.GONE
                        homeErrorText.visibility = View.VISIBLE
                        homeErrorText.text = getString(R.string.query_not_found)
                    } else {
                        homeRecyclerView.visibility = View.VISIBLE
                        homeErrorText.visibility = View.GONE
                        homeAdapter.addData(filteredList)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText == "") {
                        homeRecyclerView.visibility = View.VISIBLE
                        homeErrorText.visibility = View.GONE
                        homeAdapter.addData(marketList)
                        scrollingUp = true
                    }
                    return false
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        observeMarketData()
    }
}
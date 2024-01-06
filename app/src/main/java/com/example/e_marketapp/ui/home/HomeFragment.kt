package com.example.e_marketapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.HomeAdapter
import com.example.e_marketapp.databinding.FragmentHomeBinding
import com.example.e_marketapp.model.MarketEntity
import com.example.e_marketapp.model.BaseModel
import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.util.baseModelItemToMarketEntity
import com.example.e_marketapp.util.baseModelToMarketBasketEntity
import com.example.e_marketapp.util.clickWithDebounce
import com.example.e_marketapp.util.toastMessage
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import com.example.e_marketapp.viewmodel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val marketViewModel: MarketViewModel by viewModels()
    private val marketDbViewModel: MarketDbViewModel by activityViewModels()
    private var marketSize = 4
    private var marketList = ArrayList<BaseModelItem>()

    private var checkScrollLastPosition = true
    private var scrollingUp = true
    private var newCheckState = 1
    private lateinit var homeAdapter: HomeAdapter
    private var isFirstLoad = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeMarketData()
        searchMarketData()
        actionFragment()
    }

    private fun actionFragment(){
        binding.selectFilterButton.clickWithDebounce {
            val action = HomeFragmentDirections.actionHomeToFilterFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeMarketData() {
        lifecycleScope.launch {
            marketViewModel.marketDataState.collectLatest {
                binding.apply {
                    if (it.isLoading) {
                        homeProgressBar.visibility = View.VISIBLE
                    } else if (it.error.toString().isNotEmpty()) {
                        homeErrorText.visibility = View.VISIBLE
                        homeProgressBar.visibility = View.GONE

                        homeErrorText.text = it.error.toString()
                        tryAgainButton.visibility = View.VISIBLE

                        tryAgainButton.clickWithDebounce {
                            marketViewModel.getMarketData()
                            homeErrorText.visibility = View.GONE
                            tryAgainButton.visibility = View.GONE
                        }
                    } else {
                        homeErrorText.visibility = View.GONE
                        homeProgressBar.visibility = View.GONE
                        tryAgainButton.visibility = View.GONE
                        homeRecyclerView.visibility = View.VISIBLE

                        it.marketModel?.let { newMarketData ->
                            updateMarketList(newMarketData = newMarketData)
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

    private fun updateMarketList(newMarketData: BaseModel) {
        val filterTitleNotEmpty =
            marketDbViewModel.brandFilter.title.isNotEmpty() || marketDbViewModel.modelFilter.title.isNotEmpty()
        val brandFilterEmpty = marketDbViewModel.brandFilter.title.isEmpty()
        val modelFilterEmpty = marketDbViewModel.modelFilter.title.isEmpty()

        if (filterTitleNotEmpty && (isFirstLoad || scrollingUp)) {
            val filteredList = newMarketData.filter { item ->
                (brandFilterEmpty || item.brand == marketDbViewModel.brandFilter.title) &&
                        (modelFilterEmpty || item.model == marketDbViewModel.modelFilter.title)
            }

            val sortedList = when (marketDbViewModel.sortByText) {
                getString(R.string.old_to_new) -> filteredList.sortedBy { it.createdAt }
                getString(R.string.new_to_old) -> filteredList.sortedByDescending { it.createdAt }
                getString(R.string.price_high_to_low) -> filteredList.sortedByDescending { it.price }
                getString(R.string.price_low_to_high) -> filteredList.sortedBy { it.price }
                else -> filteredList.sortedByDescending { it.createdAt }
            }

            if (sortedList.isNotEmpty()) {
                marketList.clear()
                marketList.addAll(sortedList)
                homeAdapter.addData(newData=marketList.take(marketSize))
            } else {
                showNoResultsError()
            }
        } else if (isFirstLoad || scrollingUp) {
            val sortedList = when (marketDbViewModel.sortByText) {
                getString(R.string.old_to_new) -> newMarketData.sortedBy { it.createdAt }
                getString(R.string.new_to_old) -> newMarketData.sortedByDescending { it.createdAt }
                getString(R.string.price_high_to_low) -> newMarketData.sortedByDescending { it.price }
                getString(R.string.price_low_to_high) -> newMarketData.sortedBy { it.price }
                else -> newMarketData.sortedByDescending { it.createdAt }
            }

            if (sortedList.isNotEmpty()) {
                marketList.clear()
                marketList.addAll(sortedList)
                homeAdapter.addData(newData=marketList.take(marketSize))
            } else {
                showNoResultsError()
            }
        }
    }

    private fun showNoResultsError() {
        binding.apply {
            homeRecyclerView.visibility = View.GONE
            homeErrorText.visibility = View.VISIBLE
            homeErrorText.text = getText(R.string.query_not_found)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        homeAdapter = HomeAdapter(
            clickActionFragment = {
                val action = HomeFragmentDirections.actionHomeToDetailFragment(itemArgs = it)
                findNavController().navigate(action)
            },
            clickFavorite = { it, checkRoomDb ->
                if (checkRoomDb) {
                    marketDbViewModel.addMarketItem(
                        baseModelItemToMarketEntity(baseModelItem = it)
                    )
                } else {
                    marketDbViewModel.deleteMarketItem(marketItemId = it.id)
                }
            }, addToCardClick = {
                marketDbViewModel.addBasketItem(
                    baseModelToMarketBasketEntity(baseModelItem = it)
                )
                toastMessage(requireContext(), getString(R.string.item_added_basket))
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
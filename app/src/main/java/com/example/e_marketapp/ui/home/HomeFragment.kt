package com.example.e_marketapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.adapter.HomeAdapter
import com.example.e_marketapp.databinding.FragmentHomeBinding
import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.viewmodel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val marketViewModel: MarketViewModel by viewModels()
    private var marketSize = 4
    private var marketList = ArrayList<BaseModelItem>()
    private lateinit var homeAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMarketData()
    }

    private fun observeMarketData(){
        lifecycleScope.launch {
            marketViewModel.marketDataState.collectLatest {
                binding.apply {
                    if (it.isLoading) {
                        homeProgressBar.visibility=View.VISIBLE
                    }
                    else if (it.error.toString().isNotEmpty()) {
                        homeErrorText.visibility=View.VISIBLE
                        homeProgressBar.visibility=View.GONE
                        homeErrorText.text=it.error.toString()
                    }
                    else {
                        homeErrorText.visibility=View.GONE
                        homeProgressBar.visibility=View.GONE
                        homeRecyclerView.visibility=View.VISIBLE
                        it.marketModel?.let { newMarketData ->
                            initAdapter()
                            marketList.addAll(newMarketData)
                            homeAdapter.addMoreData(marketList.take(4))
                            println(marketList.size)
                        }
                    }
                }
            }
        }
    }

    private fun initAdapter(){
        homeAdapter = HomeAdapter()
        binding.homeRecyclerView.adapter = homeAdapter
        binding.homeRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)

        var scrollingUp=true
        var newCheckState=1
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
                if (newState %newCheckState == 0 && scrollingUp) {
                    newCheckState+=1
                    marketSize += 4
                    //Bu kontrolü saglama sebebim kullanıcı en sona geldiğinde kaydırmaya devam ederse ekleme işlemini yapıyor.Bundan dolayı son size = olunca artık çalışmayacak
                    //Sadece liste içinde değil api olsaydı sürekli istek atarak api isteğini şişiricekti önemli bir kontrol !
                    if (marketSize != marketList.size){
                        //Bu fonksiyonun içinde println var aşağı kaydırdıkça eklediğini logcatta gözlemleyebilirsiniz.
                        homeAdapter.addMoreData(marketList.take(marketSize))
                    }
                }
            }
        })
    }
}
package com.example.e_marketapp.ui.basket

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.BasketAdapter
import com.example.e_marketapp.databinding.FragmentBasketBinding
import com.example.e_marketapp.states.BasketState
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment : BaseFragment<FragmentBasketBinding>(FragmentBasketBinding::inflate){

    private val marketDbViewModel : MarketDbViewModel by viewModels()
    private lateinit var basketAdapter: BasketAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeData()
    }
    //şuan sadece anasayfadaki addtocard butonu çalışıyor.
    private fun initAdapter(){
        basketAdapter= BasketAdapter(
            plusClick = {
                marketDbViewModel.addBasketItem(it)
            },
            minusClick = {
                marketDbViewModel.deleteBasketItem(it)
            }
        )
        binding.basketRecyclerView.adapter=basketAdapter
        binding.basketRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    }

    private fun observeData(){
        with(marketDbViewModel){
            getBasketItems()
            marketDbViewModel.viewModelScope.launch {
                basketState.collectLatest {
                    binding.apply {
                        if (it.loading==true){
                            progressBar.visibility=View.VISIBLE
                        }
                        else if (it.error?.isNotBlank() == true){
                            progressBar.visibility=View.INVISIBLE
                            errorText.visibility=View.VISIBLE
                            errorText.text=it.error.toString()
                        }
                        else if (it.basketData?.isNotEmpty() == true){
                            progressBar.visibility=View.INVISIBLE
                            errorText.visibility=View.INVISIBLE
                            basketRecyclerView.visibility=View.VISIBLE
                            totalText.visibility=View.VISIBLE
                            totalPrice.visibility=View.VISIBLE
                            completeBasket.visibility=View.VISIBLE
                            setTotalPrice(it)
                        }
                         else{
                             progressBar.visibility=View.INVISIBLE
                             errorText.visibility=View.VISIBLE
                             errorText.text=getString(R.string.no_items_in_basket)
                        }
                    }
                }
            }
        }
    }

    private fun setTotalPrice(it: BasketState){
        basketAdapter.setTotalPriceListener(object  : BasketAdapter.TotalPriceListener{
            override fun onTotalPriceUpdated(totalPrice: Double) {
                binding.totalPrice.text= totalPrice.toString()
                println(totalPrice)
            }
        })
        it.basketData?.let { it1 -> basketAdapter.setBasketData(it1) }
        basketAdapter.notifyDataSetChanged()
    }


}
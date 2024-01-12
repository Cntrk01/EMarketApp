package com.example.e_marketapp.ui.basket

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.BasketAdapter
import com.example.e_marketapp.databinding.FragmentBasketBinding
import com.example.e_marketapp.model.HistoryOrderEntity
import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.states.BasketState
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.util.clickWithDebounce
import com.example.e_marketapp.util.mapBasketToHistoryOrder
import com.example.e_marketapp.util.toastMessage
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment : BaseFragment<FragmentBasketBinding>(FragmentBasketBinding::inflate){

    private val marketDbViewModel : MarketDbViewModel by viewModels()
    private lateinit var basketAdapter: BasketAdapter
    private var itemList = ArrayList<MarketBasketEntity>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeData()
        clickCompleteButton()
    }

    private fun clickCompleteButton(){
        binding.apply {
            completeBasket.clickWithDebounce {
                marketDbViewModel.apply {
                    deleteAllBasket()
                    getBasketItems()
                    lifecycleScope.launch {
                        basketState.collectLatest {
                            if (it.loading==true){
                                progressBar.visibility=View.VISIBLE
                            }
                            if (it.error !=null){
                                progressBar.visibility=View.INVISIBLE
                                basketRecyclerView.visibility=View.INVISIBLE
                                errorText.visibility=View.VISIBLE
                                errorText.text=it.error
                            }
                            if (it.basketAllDeleted?.isNotBlank() == true){
                                addHistoryOrder(mapBasketToHistoryOrder(itemList))
                                setVisibleItem(4)
                                binding.errorText.visibility=View.VISIBLE
                                binding.errorText.text=getString(R.string.your_order_is_completed)
                            }else{
                                binding.errorText.visibility=View.VISIBLE
                                binding.errorText.text=getString(R.string.your_order_is_completed)
                            }
                        }
                    }
                }
            }
        }
    }
    private fun observeData(){
        with(marketDbViewModel){
            getBasketItems()
            lifecycleScope.launch {
                basketState.collectLatest {
                    binding.apply {
                        if (it.loading==true){
                            progressBar.visibility=View.VISIBLE
                        }
                        else if (it.error?.isNotBlank() == true){
                            progressBar.visibility=View.INVISIBLE
                            basketRecyclerView.visibility=View.INVISIBLE
                            errorText.visibility=View.VISIBLE
                            errorText.text=it.error.toString()
                        }
                        else if (it.basketData?.isNotEmpty() == true){
                            progressBar.visibility=View.INVISIBLE
                            errorText.visibility=View.INVISIBLE
                            setVisibleItem(0)
                            setTotalPrice(it)
                            itemList.addAll(it.basketData)
                        }
                        else if (it.basketData?.isNotEmpty() == false){
                            setVisibleItem(4)
                            progressBar.visibility=View.INVISIBLE
                            errorText.visibility=View.VISIBLE
                            errorText.text=getString(R.string.no_items_in_basket)
                        }
                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter(){
        basketAdapter= BasketAdapter(
            plusClick = {
                marketDbViewModel.addBasketItem(it)
            },
            minusClick = {
                marketDbViewModel.deleteBasketItem(it)
            },
        )
        basketAdapter.notifyDataSetChanged()
        binding.basketRecyclerView.adapter=basketAdapter
        binding.basketRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    }
    
    private fun setVisibleItem(visible:Int){
        binding.apply {
            basketRecyclerView.visibility=visible
            completeBasket.visibility=visible
            totalText.visibility=visible
            totalPrice.visibility=visible
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setTotalPrice(it: BasketState){
        basketAdapter.setTotalPriceListener(object  : BasketAdapter.TotalPriceListener{
            override fun onTotalPriceUpdated(totalPrice: Int) {
                binding.totalPrice.text = totalPrice.toString()
            }
        })
        it.basketData?.let { it1 -> basketAdapter.setBasketData(list=it1) }
        basketAdapter.notifyDataSetChanged()
    }
}
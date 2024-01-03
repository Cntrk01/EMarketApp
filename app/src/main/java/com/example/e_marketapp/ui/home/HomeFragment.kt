package com.example.e_marketapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_marketapp.databinding.FragmentHomeBinding
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.viewmodel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val marketViewModel: MarketViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            marketViewModel.marketDataState.collectLatest {
                if (it.isLoading) {
                    println("LOADING")
                }
                else if (it.error.toString().isNotEmpty()) {
                    println("ERROR")
                }
                else {
                    println(it.marketModel)
                }
            }
        }
    }
}
package com.example.e_marketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_marketapp.model.BaseModel
import com.example.e_marketapp.states.MarketState
import com.example.e_marketapp.usecase.MarketUseCase
import com.example.e_marketapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(private val marketUseCase: MarketUseCase) : ViewModel() {
    private val _marketDataState = MutableStateFlow(MarketState())
    val marketDataState: StateFlow<MarketState> get() = _marketDataState

    init {
        getMarketData()
    }

    private fun getMarketData() = viewModelScope.launch {
        marketUseCase.getMarketData().collect { response ->
            when (response) {
                is Response.Loading -> {
                    _marketDataState.update {
                        it.copy(isLoading = true, error = "", marketModel = null)
                    }
                }
                is Response.Error -> {
                    _marketDataState.update {
                        it.copy(error = response.message, isLoading = false, marketModel = null)
                    }
                }
                is Response.Success -> {
                    _marketDataState.update {
                        it.copy(marketModel = response.data,isLoading = false, error = "")
                    }
                }
            }
        }
    }
}
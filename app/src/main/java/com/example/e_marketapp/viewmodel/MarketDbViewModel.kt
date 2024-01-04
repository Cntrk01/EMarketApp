package com.example.e_marketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_marketapp.local.MarketEntity
import com.example.e_marketapp.states.MarketDbState
import com.example.e_marketapp.usecase.MarketDbUseCase
import com.example.e_marketapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketDbViewModel @Inject constructor(private val marketDbUseCase: MarketDbUseCase) :
    ViewModel() {

    private var _getAllData = MutableStateFlow(MarketDbState())
    val getAllData: StateFlow<MarketDbState> get() = _getAllData

    init {
        getAllItems()
    }

    private fun getAllItems() = viewModelScope.launch {
        //burada channelFlow kullandım.Çünkü flow olarak return etmeye çalıştığımda datayı emit etmeye çalışıyor sonrasında
        // catch blogu calıstıgı için emit etmeye çalışıyor.Bundan dolayı emit edilmiyor.
        //ben normal try catch kullandım flowun catchini kullandıgımda hata gözlemlemedim fakat channelFlow kullandım.
        marketDbUseCase.getAllItems().collectLatest {
            when (it) {
                is Response.Loading -> {
                    _getAllData.update { dbState ->
                        dbState.copy(error = null, marketData = null, loading = true)
                    }
                }

                is Response.Error -> {
                    _getAllData.update { dbState ->
                        dbState.copy(
                            error = it.message.toString(),
                            marketData = null,
                            loading = false
                        )
                    }
                }

                else -> {
                    _getAllData.update { dbState ->
                        dbState.copy(error = null, marketData = it.data, loading = false)
                    }
                }
            }
        }
    }

    fun addMarketItem(marketItem: MarketEntity) = viewModelScope.launch {
        marketDbUseCase.addMarketItem(marketItem).collectLatest {
            when (it) {
                is Response.Loading -> {
                    _getAllData.update { dbState ->
                        dbState.copy(
                            isAdded = false,
                            error = null,
                            marketData = null,
                            loading = true,
                            isDeleted = false
                        )
                    }
                }

                is Response.Error -> {
                    _getAllData.update { dbState ->
                        dbState.copy(
                            isAdded = false,
                            error = it.message.toString(),
                            marketData = null,
                            loading = false,
                            isDeleted = false
                        )
                    }
                }

                else -> {
                    _getAllData.update { dbState ->
                        dbState.copy(
                            isAdded = true,
                            error = null,
                            marketData = null,
                            loading = false,
                            isDeleted = false
                        )
                    }
                }
            }
        }
    }

    fun deleteMarketItem(marketItemId: String) = viewModelScope.launch{
        marketDbUseCase.deleteMarketItem(marketItemId).collectLatest {
            when (it) {
                is Response.Loading -> {
                    _getAllData.value=MarketDbState(loading = true, isDeleted = false)
                }

                is Response.Error -> {
                    _getAllData.value=MarketDbState(error = it.message.toString(), isDeleted = false, loading = false)
                }

                else -> {
                    _getAllData.value= MarketDbState(isDeleted = true)
                }
            }
        }
    }

    fun getMealClickedItem(clickMarketId:String) = viewModelScope.launch {
        marketDbUseCase.getMealClickedItem(clickMarketId).collectLatest {
            when (it) {
                is Response.Loading -> {
                    _getAllData.update { dbState ->
                        dbState.copy(error = null, marketData = null, loading = true, isDeleted = null)
                    }
                }

                is Response.Error -> {
                    _getAllData.update { dbState ->
                        dbState.copy(
                            error = it.message.toString(), marketData = null, loading = false, isDeleted = null
                        )
                    }
                }
                else -> {
                    _getAllData.update { dbState ->
                        dbState.copy(error = null, marketDataWithId = it.data, loading = false, isDeleted = null,  marketData= null)
                    }
                }
            }
        }
    }


}
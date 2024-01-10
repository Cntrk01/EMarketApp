package com.example.e_marketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.model.MarketEntity
import com.example.e_marketapp.model.FilterModelItem
import com.example.e_marketapp.model.HistoryOrderEntity
import com.example.e_marketapp.model.HistoryOrderModel
import com.example.e_marketapp.states.BasketState
import com.example.e_marketapp.states.MarketDbState
import com.example.e_marketapp.usecase.MarketDbUseCase
import com.example.e_marketapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketDbViewModel @Inject constructor(private val marketDbUseCase: MarketDbUseCase) : ViewModel() {

    var sortByText: String = ""
    var brandFilter: FilterModelItem = FilterModelItem("",false)
    var modelFilter: FilterModelItem = FilterModelItem("",false)

    private var _getAllData = MutableStateFlow(MarketDbState())
    val getAllData: StateFlow<MarketDbState> get() = _getAllData

    private var _basketState = MutableStateFlow(BasketState())
    val basketState : StateFlow<BasketState> get() = _basketState

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

    fun getBasketItems() = viewModelScope.launch {
        marketDbUseCase.getBasketItems().collectLatest {
            when(it){
                is Response.Loading->{
                    _basketState.value=BasketState(loading = true)
                }
                is Response.Error->{
                    _basketState.value=BasketState(loading = false, error = it.message.toString())
                }
                else ->{
                    _basketState.value=BasketState(loading = false, error ="", basketData = it.data)
                }
            }
        }
    }

    fun addBasketItem(basketEntity: MarketBasketEntity) = viewModelScope.launch {
        marketDbUseCase.addBasketItems(basketEntity = basketEntity).collectLatest {
            when(it){
                is Response.Loading->{
                    _basketState.value=BasketState(loading = true)
                }
                is Response.Error->{
                    _basketState.value=BasketState(loading = false, error = it.message.toString())
                }
                else ->{
                    _basketState.value=BasketState(loading = false, error ="", basketAdded = "Added To Basket..")
                }
            }
        }
    }

    fun deleteBasketItem(basketEntity: MarketBasketEntity) = viewModelScope.launch {
        marketDbUseCase.minusProductCount(basketEntity = basketEntity).collectLatest {
            when(it){
                is Response.Loading->{
                    _basketState.value=BasketState(loading = true)
                }
                is Response.Error->{
                    _basketState.value=BasketState(loading = false, error = it.message.toString())
                }
                else ->{
                    _basketState.value=BasketState(loading = false, error ="", basketAdded = "Deleted To Basket..")
                }
            }
        }
    }

    fun deleteAllBasket() = viewModelScope.launch{
        marketDbUseCase.deleteAllBasket().collectLatest {
            when(it){
                is Response.Loading->{
                    _basketState.value=BasketState(loading = true)
                }
                is Response.Error->{
                    _basketState.value=BasketState(loading = false, error = "Process Is Not Completed...")
                }
                else ->{
                    _basketState.value=BasketState(loading = false, error ="", basketAllDeleted = "Deleted To Basket..")
                }
            }
        }
    }

    fun addHistoryOrder(historyOrder: List<HistoryOrderEntity>) = viewModelScope.launch {
        marketDbUseCase.addHistoryOrder(historyOrder = historyOrder).collectLatest {
            when(it){
                is Response.Loading->{
                    _basketState.value=BasketState(loading = true)
                }
                is Response.Error->{
                    _basketState.value=BasketState(loading = false, error = "Not Added")
                }
                else ->{
                    _basketState.value=BasketState(loading = false, error ="")
                }
            }
        }
    }

    fun getHistoryOrder () = viewModelScope.launch {
        marketDbUseCase.getHistoryOrder().collectLatest {
            when(it){
                is Response.Loading->{
                    _basketState.value=BasketState(loading = true)
                }
                is Response.Error->{
                    _basketState.value=BasketState(loading = false, error = "Not Added")
                }
                else ->{
                    _basketState.value=BasketState(loading = false, error ="", historyOrderModel = it.data)
                }
            }
        }
    }


}
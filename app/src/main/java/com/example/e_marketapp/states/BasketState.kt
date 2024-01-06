package com.example.e_marketapp.states

import com.example.e_marketapp.model.HistoryOrderModel
import com.example.e_marketapp.model.MarketBasketEntity

data class BasketState(
    val loading : Boolean ?=false,
    val error : String ?=null,
    val basketData : List<MarketBasketEntity> ?=null,
    val basketAdded: String ?="",
    val basketAllDeleted : String ?=null,
    val historyOrderModel: List<HistoryOrderModel> ?=null
)

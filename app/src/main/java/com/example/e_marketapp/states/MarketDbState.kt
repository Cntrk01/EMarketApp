package com.example.e_marketapp.states

import com.example.e_marketapp.model.MarketEntity

data class MarketDbState(
    val loading : Boolean ?=false,
    val error : String ?= null,
    val marketData : List<MarketEntity> ?=null,
    val isAdded : Boolean ?= false,
    val isDeleted : Boolean ?=null,
    val marketDataWithId : MarketEntity?=null
)

package com.example.e_marketapp.states

import com.example.e_marketapp.model.BaseModel

data class MarketState(
    val isLoading : Boolean=false,
    var marketModel : BaseModel ?= null,
    val error : String ?= "",
)
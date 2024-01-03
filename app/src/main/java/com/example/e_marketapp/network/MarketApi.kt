package com.example.e_marketapp.network

import com.example.e_marketapp.model.BaseModel
import retrofit2.http.GET

interface MarketApi {
    @GET("products")
    suspend fun getEMarketData() : BaseModel
}
package com.example.e_marketapp.usecase

import com.example.e_marketapp.model.BaseModel
import com.example.e_marketapp.repository.MarketRepositoryImpl
import com.example.e_marketapp.util.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketUseCase @Inject constructor(private val repositoryImpl: MarketRepositoryImpl) {
    suspend fun getMarketData() : Flow<Response<BaseModel>> = repositoryImpl.getMarketData()
}
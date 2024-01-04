package com.example.e_marketapp.usecase

import com.example.e_marketapp.local.MarketEntity
import com.example.e_marketapp.repository.MarketDbRepositoryImpl
import com.example.e_marketapp.util.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketDbUseCase @Inject constructor(private val marketDbRepo : MarketDbRepositoryImpl) {
    fun getAllItems(): Flow<Response<List<MarketEntity>>> = marketDbRepo.getAllItems()
    suspend fun addMarketItem(marketItem: MarketEntity): Flow<Response<Boolean>> = marketDbRepo.addMarketItem(marketItem = marketItem)
    suspend fun deleteMarketItem(foodId: String) : Flow<Response<Boolean>> = marketDbRepo.deleteMarketItem(foodId = foodId)
    suspend fun getMealClickedItem(clickMarketId:String) :  Flow<Response<MarketEntity>> = marketDbRepo.getMealClickedItem(clickMarketId=clickMarketId)
}
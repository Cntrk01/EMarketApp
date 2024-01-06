package com.example.e_marketapp.usecase

import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.model.MarketEntity
import com.example.e_marketapp.repository.MarketDbRepositoryImpl
import com.example.e_marketapp.util.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketDbUseCase @Inject constructor(private val marketDbRepo : MarketDbRepositoryImpl) {
    fun getAllItems(): Flow<Response<List<MarketEntity>>> = marketDbRepo.getAllItems()
    suspend fun addMarketItem(marketItem: MarketEntity): Flow<Response<Boolean>> = marketDbRepo.addMarketItem(marketItem = marketItem)
    suspend fun deleteMarketItem(foodId: String) : Flow<Response<Boolean>> = marketDbRepo.deleteMarketItem(foodId = foodId)
    suspend fun getMealClickedItem(clickMarketId:String) :  Flow<Response<MarketEntity>> = marketDbRepo.getMealClickedItem(clickMarketId=clickMarketId)

    fun getBasketItems() : Flow<Response<List<MarketBasketEntity>>> = marketDbRepo.getBasketItems()
    suspend fun addBasketItems(basketEntity: MarketBasketEntity): Flow<Response<Unit>> = marketDbRepo.addBasketItems(basketEntity = basketEntity)
    suspend fun minusProductCount(basketEntity: MarketBasketEntity) :  Flow<Response<Unit>> = marketDbRepo.minusProductCount(basketEntity)
}
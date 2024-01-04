package com.example.e_marketapp.repository

import com.example.e_marketapp.local.MarketBasketEntity
import com.example.e_marketapp.local.MarketDao
import com.example.e_marketapp.local.MarketEntity
import com.example.e_marketapp.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarketDbRepositoryImpl @Inject constructor(private val dao: MarketDao) {

    fun getAllItems(): Flow<Response<List<MarketEntity>>> {
        return channelFlow {
            try {
                trySend(Response.Loading())
                val dbData = dao.getAllItems()
                dbData.collectLatest {
                    trySend(Response.Success(data = it))
                }
            } catch (e: Exception) {
                trySend(Response.Error(message = e.message.toString() ?: ""))
            }
            awaitClose()
        }
    }

    suspend fun addMarketItem(marketItem: MarketEntity): Flow<Response<Boolean>> {
        return flow {
            try {
                emit(Response.Loading())
                dao.addMarketItem(marketItem=marketItem)
                emit(Response.Success(data = true))
            }catch (e: Exception) {
                emit(Response.Error(message = e.message.toString() ?: ""))
            }
        }
    }

    suspend fun deleteMarketItem(foodId: String) : Flow<Response<Boolean>> {
        return channelFlow {
            try {
                trySend(Response.Loading())
                dao.deleteMarketItem(deletedItem = foodId)
                trySend(Response.Success(data = true))
            }catch (e: Exception) {
                trySend(Response.Error(message = e.message.toString() ?: ""))
            }
            awaitClose()
        }
    }

    suspend fun getMealClickedItem(clickMarketId:String) :  Flow<Response<MarketEntity>>{
        return flow {
            try {
                emit(Response.Loading())
                val getMarketItem=dao.getMarketItemWithId(clickMarketId = clickMarketId)
                if (clickMarketId==getMarketItem.marketId){
                    emit(Response.Success(getMarketItem))
                }
            }catch (e: Exception) {
                emit(Response.Error(message = e.message.toString() ?: ""))
            }
        }
    }

    fun getBasketItems() : Flow<Response<List<MarketBasketEntity>>>{
        return channelFlow {
            try {
                trySend(Response.Loading())
                val dbData = dao.getBasketItems()
                dbData.collectLatest {
                    trySend(Response.Success(data = it))
                }
            } catch (e: Exception) {
                trySend(Response.Error(message = e.message.toString() ?: ""))
            }
            awaitClose()
        }
    }

    suspend fun addBasketItems(basketEntity: MarketBasketEntity): Flow<Response<Unit>>{
        return flow {
            try {
                emit(Response.Loading())
                val existingItem = dao.getBasketItem(basketEntity.productId)
                if (existingItem != null) {
                    val totalPrice=basketEntity.singleItemPrice.toDouble() * (existingItem.productCount.toDouble()+1.00)
                    dao.plusProductCount(basketEntity.productId,1, totalPrice)
                } else {
                    dao.addBasketItems(basketEntity = basketEntity)
                }
                emit(Response.Success(Unit))
            } catch (e: Exception) {
                emit(Response.Error(message = e.message.toString() ?: ""))
            }
        }
    }

    suspend fun minusProductCount(basketEntity: MarketBasketEntity) : Flow<Response<Unit>>{
        return flow {
            try {
                emit(Response.Loading())
                val existingItem = dao.getBasketItem(basketEntity.productId)

                if (existingItem != null) {
                    val newCount = existingItem.productCount - 1
                    if (newCount >= 0) {
                        val totalPrice = basketEntity.singleItemPrice.toDouble() * newCount
                        dao.minusProductCount(basketEntity.productId, 1, totalPrice)
                        emit(Response.Success(Unit))
                    } else {
                        emit(Response.Error(message = "Item Size 0"))
                    }
                } else {
                    emit(Response.Error(message = "Item not found"))
                }
            } catch (e: Exception) {
                emit(Response.Error(message = e.message.toString() ?: ""))
            }
        }
    }

}
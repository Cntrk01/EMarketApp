package com.example.e_marketapp.repository

import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.local.MarketDao
import com.example.e_marketapp.model.HistoryOrderEntity
import com.example.e_marketapp.model.HistoryOrderModel
import com.example.e_marketapp.model.MarketEntity
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
                    emit(Response.Success(data = getMarketItem))
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
                val existingItem = dao.getSingleBasketItem(basketEntity.productId)
                if (existingItem != null) {
                    val totalPrice=basketEntity.singleItemPrice * (existingItem.productCount)
                    dao.plusBasketItemCount(basketEntity.productId,1, totalPrice)
                } else {
                    dao.addBasketItems(basketEntity = basketEntity)
                }
                emit(Response.Success(Unit))
            } catch (e: Exception) {
                emit(Response.Error(message = e.message.toString() ?: ""))
            }
        }
    }

    suspend fun minusBasketItemCount(basketEntity: MarketBasketEntity) : Flow<Response<Unit>>{
        return flow {
            try {
                emit(Response.Loading())
                val existingItem = dao.getSingleBasketItem(basketEntity.productId)

                if (existingItem != null) {
                    val newCount = existingItem.productCount - 1
                    if (newCount >= 1) {
                        val totalPrice = basketEntity.singleItemPrice.toInt() * newCount
                        dao.minusBasketItemCount(basketEntity.productId, 1, totalPrice)
                        emit(Response.Success(data = Unit))
                    } else if (newCount==0){
                        dao.deleteProduct(basketEntity.productId)
                    }
                } else {
                    emit(Response.Error(message = "Item not found"))
                }
            } catch (e: Exception) {
                emit(Response.Error(message = e.message.toString() ?: ""))
            }
        }
    }

    suspend fun deleteAllBasket() :  Flow<Response<Unit>>{
        return flow {
            try {
                emit(Response.Loading())
                dao.deleteAllBasket()
                emit(Response.Success(data = Unit))
            }catch (e:Exception){
                emit(Response.Error(message = "Process Is Not Completed..."))
            }
        }
    }

    suspend fun addHistoryOrder(historyOrder: List<HistoryOrderEntity>):  Flow<Response<Unit>>{
        return flow {
            try {
                emit(Response.Loading())
                val historyOrderModel = HistoryOrderModel(historyList = historyOrder)
                dao.addHistoryOrder(historyOrderModel)
                emit(Response.Success(data = Unit))
            }catch (e:Exception){
                emit(Response.Error(message = "Error"))
            }
        }
    }

    suspend fun getHistoryOrder () :  Flow<Response<List<HistoryOrderModel>>> {
        return channelFlow {
            try {
                trySend(Response.Loading())
                val getHistoryOrder=dao.getHistoryOrder()
                getHistoryOrder.collectLatest {
                    trySend(Response.Success(data = it))
                }
            }catch (e:Exception){
                trySend(Response.Error(message = "Error"))
            }
            awaitClose()
        }
    }


}
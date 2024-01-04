package com.example.e_marketapp.repository

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

}
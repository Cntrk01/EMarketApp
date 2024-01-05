package com.example.e_marketapp.repository

import com.example.e_marketapp.model.BaseModel
import com.example.e_marketapp.network.MarketApi
import com.example.e_marketapp.util.Response
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(private val api : MarketApi)  {

    suspend fun getMarketData () : Flow<Response<BaseModel>>{
        return flow {
            try {
                emit(Response.Loading())
                val data=api.getEMarketData()
                if (data.isNotEmpty()){
                    emit(Response.Success(data = data))
                }
            }catch (e:Exception){
                emit(Response.Error(message = "Error !"))
            }catch (e: TimeoutCancellationException) {
                emit(Response.Error("Operation timed out !"))
            }
        }
    }
}
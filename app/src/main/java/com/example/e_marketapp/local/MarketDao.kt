package com.example.e_marketapp.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {
    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<MarketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMarketItem(marketItem: MarketEntity)

    @Query("DELETE FROM items WHERE items.marketId= :deletedItem")
    suspend fun deleteMarketItem(deletedItem:String)

    @Query("SELECT * FROM items WHERE items.marketId=:clickMarketId")
    suspend fun getMarketItemWithId(clickMarketId:String) : MarketEntity
}
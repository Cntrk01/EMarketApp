package com.example.e_marketapp.local

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT * FROM basket")
    fun getBasketItems(): Flow<List<MarketBasketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBasketItems(basketEntity: MarketBasketEntity)

    @Query("UPDATE basket SET productPrice= :productPrice, productCount = productCount + :count WHERE productId = :productId")
    suspend fun plusProductCount(productId: String, count: Int,productPrice:Double)

    @Query("UPDATE basket SET productPrice= :productPrice, productCount = productCount - :count WHERE productId = :productId")
    suspend fun minusProductCount(productId: String, count: Int, productPrice: Double)

    @Query("SELECT * FROM basket WHERE productId = :productId LIMIT 1")
    suspend fun getBasketItem(productId: String): MarketBasketEntity?

    @Query("DELETE FROM basket WHERE basket.productId= :deleteItem")
    suspend fun deleteProduct(deleteItem:String)
}
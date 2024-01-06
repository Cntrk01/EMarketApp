package com.example.e_marketapp.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.e_marketapp.model.HistoryOrderModel
import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.model.MarketEntity
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
    suspend fun plusBasketItemCount(productId: String, count: Int, productPrice:Int)

    @Query("UPDATE basket SET productPrice= :productPrice, productCount = productCount - :count WHERE productId = :productId")
    suspend fun minusBasketItemCount(productId: String, count: Int, productPrice: Int)

    @Query("SELECT * FROM basket WHERE productId = :productId LIMIT 1")
    suspend fun getSingleBasketItem(productId: String): MarketBasketEntity?

    @Query("DELETE FROM basket WHERE basket.productId= :deleteItem")
    suspend fun deleteProduct(deleteItem:String)

    @Query("DELETE FROM basket")
    suspend fun deleteAllBasket()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistoryOrder(historyOrder: HistoryOrderModel)

    @Query("SELECT * FROM historyOrderList")
    fun getHistoryOrder () :  Flow<List<HistoryOrderModel>>
}
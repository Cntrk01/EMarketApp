package com.example.e_marketapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket")
data class MarketBasketEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int ?=null,
    val productId: String,
    val productName:String,
    val singleItemPrice:String,
    var productPrice:Double,
    var productCount:Int
)
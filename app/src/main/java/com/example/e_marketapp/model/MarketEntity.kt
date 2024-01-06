package com.example.e_marketapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class MarketEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int ?=null,
    val marketId: String,
    val brand: String,
    val createdAt: String,
    val description: String,
    val image: String,
    val model: String,
    val name: String,
    val price: String
){
    override fun hashCode(): Int {
        return marketId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MarketEntity

        if (marketId != other.marketId) return false

        return true
    }
}

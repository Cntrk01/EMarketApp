package com.example.e_marketapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.model.MarketEntity

@Database(entities = [MarketEntity::class, MarketBasketEntity::class], version = 2)
abstract class MarketDatabase  : RoomDatabase(){
    abstract fun marketDao() : MarketDao
}
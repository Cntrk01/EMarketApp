package com.example.e_marketapp.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarketEntity::class,MarketBasketEntity::class], version = 2)
abstract class MarketDatabase  : RoomDatabase(){
    abstract fun marketDao() : MarketDao
}
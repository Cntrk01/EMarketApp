package com.example.e_marketapp.module

import android.content.Context
import androidx.room.Room
import com.example.e_marketapp.local.MarketDao
import com.example.e_marketapp.local.MarketDatabase
import com.example.e_marketapp.repository.MarketDbRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, MarketDatabase::class.java, "MarketDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideProductsDao(database: MarketDatabase) = database.marketDao()

    @Singleton
    @Provides
    fun provideMarketDbRepository(dao: MarketDao) = MarketDbRepositoryImpl(dao = dao)
}
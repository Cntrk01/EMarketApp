package com.example.e_marketapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.RawValue

@Entity(tableName = "historyOrderList")
@TypeConverters(TypeConventer::class)
@kotlinx.parcelize.Parcelize
data class HistoryOrderModel (
    @PrimaryKey(autoGenerate = true)
    val id : Int ?=null,
    val historyList : @RawValue ArrayList<HistoryOrderEntity> ?=null
) : Parcelable


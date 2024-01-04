package com.example.e_marketapp.model

import android.os.Parcelable
import androidx.room.Entity

@kotlinx.parcelize.Parcelize
data class BaseModelItem(
    val brand: String,
    val createdAt: String,
    val description: String,
    val id: String,
    val image: String,
    val model: String,
    val name: String,
    val price: String
) : Parcelable
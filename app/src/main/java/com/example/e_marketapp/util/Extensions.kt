package com.example.e_marketapp.util

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide


fun popBackStack(view:View){
    findNavController(view).popBackStack()
}

fun ImageView.urlToImageGlide(url: String){
    Glide.with(this.context)
        .load(url)
        .into(this)
}
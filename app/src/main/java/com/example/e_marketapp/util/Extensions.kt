package com.example.e_marketapp.util

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.Toast
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

fun View.clickWithDebounce(debounceTime: Long = 1000L, action: (View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action(v)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun toastMessage(context:Context,message:String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}


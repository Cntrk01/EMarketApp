package com.example.e_marketapp.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.e_marketapp.R
import com.example.e_marketapp.databinding.CustomToolBarBinding
import com.example.e_marketapp.util.clickWithDebounce

class CustomToolBar(context: Context, attrs: AttributeSet) : ConstraintLayout(
    context, attrs
) {
    fun navigationIconSetOnClickListener(function: (view: View) -> Unit) {
        navigationIconSetOnClickListener = function
    }

    private var navigationIconSetOnClickListener: (view: View) -> Unit = {}

    init {
        inflate(context, R.layout.custom_tool_bar, this)
        val binding = CustomToolBarBinding.bind(this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar)
        val navigationIcon = attributes.getDrawable(R.styleable.CustomToolBar_navigation_icon)
        val navigationIconVisibility = attributes.getBoolean(R.styleable.CustomToolBar_navigation_icon_visibility, true)

        val textColor = attributes.getColor(
            R.styleable.CustomToolBar_toolBarTextColor, context.getColor(R.color.black)
        )

        binding.navigationIcon.apply {
            setImageDrawable(navigationIcon)
            visibility = if (navigationIconVisibility) View.VISIBLE else View.GONE
            clickWithDebounce {
                navigationIconSetOnClickListener.invoke(it)
            }
        }

        binding.toolbarText.apply {
            text = attributes.getString(R.styleable.CustomToolBar_toolBarText)
            setTextColor(textColor)
        }

        binding.toolbarDetailText.apply {
            text = attributes.getString(R.styleable.CustomToolBar_toolBarText)
            setTextColor(textColor)
        }

        attributes.recycle()
    }
}
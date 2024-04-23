package com.example.e_marketapp.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.e_marketapp.R
import com.example.e_marketapp.databinding.CustomToolBarBinding
import com.example.e_marketapp.util.clickWithDebounce

class CustomToolBar(context: Context, attrs: AttributeSet) : ConstraintLayout(
    context, attrs
) {
    val navigationIconSetOnClickListener: ((() -> Unit)) -> Unit = { function ->
        navigationIconSetOnClickListener1 = function
    }

    private var navigationIconSetOnClickListener1: () -> Unit = {}

    init {
        inflate(context, R.layout.custom_tool_bar, this)
        val binding = CustomToolBarBinding.bind(this)
        val constraintLayout : ConstraintLayout=findViewById(R.id.constraintLayout)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar)
        val navigationIcon = attributes.getDrawable(R.styleable.CustomToolBar_navigation_icon)
        val navigationIconVisibility = attributes.getBoolean(R.styleable.CustomToolBar_navigation_icon_visibility, true)
        val toolBarFirstTextVisibilty=attributes.getBoolean(R.styleable.CustomToolBar_toolBarFirstTextVisibility,false)
        val toolBarSecondTextVisibilty=attributes.getBoolean(R.styleable.CustomToolBar_toolBarSecondTextVisibility,false)
        val textColor = attributes.getColor(R.styleable.CustomToolBar_toolBarTextColor, context.getColor(R.color.black))
        val backgroundColor = attributes.getColor(R.styleable.CustomToolBar_toolBarBackgroundColor, context.getColor(R.color._2A59FE))

        constraintLayout.setBackgroundColor(backgroundColor)

        binding.navigationIcon.apply {
            setImageDrawable(navigationIcon)
            visibility = if (navigationIconVisibility) View.VISIBLE else View.GONE
            clickWithDebounce {
                navigationIconSetOnClickListener1()
            }
        }

        binding.toolbarText.apply {
            visibility = if (toolBarFirstTextVisibilty) View.VISIBLE else View.GONE
            text = attributes.getString(R.styleable.CustomToolBar_toolBarText)
            setTextColor(textColor)
        }

        binding.toolbarDetailText.apply {
            visibility=if (toolBarSecondTextVisibilty) View.VISIBLE else View.GONE
            text = attributes.getString(R.styleable.CustomToolBar_toolBarText)
            setTextColor(textColor)
        }

        binding.toolbarCenterText.apply{
            text = attributes.getString(R.styleable.CustomToolBar_toolBarCenterText)
            setTextColor(textColor)
        }

        attributes.recycle()
    }
}
package com.example.e_marketapp.component

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.e_marketapp.R

class CustomContinueButton(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.custom_continue_button, this)
        val textView: TextView = findViewById(R.id.customMainButtonText)
        val constraintLayout : ConstraintLayout =findViewById(R.id.contineButtonConstraint)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomContinueButton)
        val textColor = attributes.getColor(R.styleable.CustomContinueButton_buttonTextColor, context.getColor(R.color.black))
        val fontFamily=attributes.getFont(R.styleable.CustomContinueButton_buttonFontFamily)
        //val backgroundDrawable = attributes.getDrawable(R.styleable.CustomContinueButton_buttonBackgroundDrawable)
        //val backgroundColor = attributes.getColor(R.styleable.CustomContinueButton_buttonBackgroundColor, context.getColor(R.color.transparent))

        //constraintLayout.background=backgroundDrawable
        //constraintLayout.setBackgroundColor(backgroundColor)
        textView.text=attributes.getString(R.styleable.CustomContinueButton_buttonText)
        textView.setTextColor(textColor)
        if (fontFamily.toString().isNotEmpty()) {
            val typeface = Typeface.create(fontFamily, Typeface.NORMAL)
            textView.typeface = typeface
        }
        attributes.recycle()
    }
}
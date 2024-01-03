package com.example.e_marketapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.e_marketapp.R
import com.example.e_marketapp.databinding.FragmentDetailBinding
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.util.popBackStack
import com.example.e_marketapp.util.urlToImageGlide

class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate){

    private val args : DetailFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setArgsData()
    }

    @SuppressLint("SetTextI18n")
    private fun setArgsData(){
        val customToolBarText = binding.customToolBar.findViewById<TextView>(R.id.toolbar_text)

        binding.apply {
           args.itemArgs.apply {
               detailImage.urlToImageGlide(image)
               detailTitle.text=name
               detailDescription.text=description
               detailPriceAmount.text=price

               addToCartButton.setOnClickListener {
                   clickAddToCardButton()
               }

               if (name.length>15){
                   customToolBarText.text=name.substring(0,15)+"..."
               }else{
                   customToolBarText.text=name
               }

               customToolBar.navigationIconSetOnClickListener {
                   popBackStack(requireView())
               }
           }
        }
    }

    private fun clickAddToCardButton(){

    }
}
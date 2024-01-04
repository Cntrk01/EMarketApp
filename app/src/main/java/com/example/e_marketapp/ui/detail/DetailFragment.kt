package com.example.e_marketapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.example.e_marketapp.R
import com.example.e_marketapp.databinding.FragmentDetailBinding
import com.example.e_marketapp.local.MarketEntity
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.util.clickWithDebounce
import com.example.e_marketapp.util.popBackStack
import com.example.e_marketapp.util.urlToImageGlide
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val args: DetailFragmentArgs by navArgs()
    private val marketDbViewModel: MarketDbViewModel by viewModels()
    private var roomId=0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkDbHasData()
    }

    private fun checkDbHasData() {
        marketDbViewModel.getMealClickedItem(args.itemArgs.id)
        marketDbViewModel.viewModelScope.launch {
            marketDbViewModel.getAllData.collectLatest {
                if (it.marketDataWithId != null) {
                    roomId=it.marketDataWithId.marketId.toInt()
                    println("ROOMDAN GELDİ")
                    it.marketDataWithId.apply {
                        setArgsData(
                            toolBarText = name,
                            imageUrl = image,
                            title = name,
                            description = description,
                            price = price,
                        )
                    }
                } else {
                    println("ARGS GELDİ")
                    roomId=0
                    args.itemArgs.apply {
                        setArgsData(
                            toolBarText = name,
                            imageUrl = image,
                            title = name,
                            description = description,
                            price = price,
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setArgsData(
        toolBarText: String,
        imageUrl: String,
        title: String,
        description: String,
        price: String,
    ) {
        val customToolBarText = binding.customToolBar.findViewById<TextView>(R.id.toolbar_text)

        binding.apply {
            detailImage.urlToImageGlide(imageUrl)
            detailTitle.text = title
            detailDescription.text = description
            detailPriceAmount.text = price

            addToCartButton.clickWithDebounce {
                clickAddToCardButton()
            }
            if (toolBarText.length > 15) {
                customToolBarText.text = toolBarText.substring(0, 15) + "..."
            } else {
                customToolBarText.text = toolBarText
            }

            customToolBar.navigationIconSetOnClickListener {
                popBackStack(requireView())
            }

            marketItemStar.setOnClickListener {
                clickStarIcon()
            }

        }
    }

    private fun clickStarIcon() {
        println("ARGS ID"  +args.itemArgs.id)
        println("ROOM ID"  +roomId)
        if (roomId ==0) {
            binding.marketItemStar.visibility=View.VISIBLE
            binding.marketItemUnStar.visibility=View.INVISIBLE
            args.itemArgs.apply {
                marketDbViewModel.addMarketItem(
                    MarketEntity(
                        marketId = id,
                        brand = brand,
                        createdAt = createdAt,
                        description = description,
                        image = image,
                        model = model,
                        name = name,
                        price = price
                    )
                )
            }
        } else {
            binding.marketItemStar.visibility=View.INVISIBLE
            binding.marketItemUnStar.visibility=View.VISIBLE
            marketDbViewModel.deleteMarketItem(roomId.toString())

        }
    }

    private fun clickAddToCardButton() {

    }
}
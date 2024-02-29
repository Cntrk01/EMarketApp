package com.example.e_marketapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.example.e_marketapp.R
import com.example.e_marketapp.databinding.FragmentDetailBinding
import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.model.MarketEntity
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.util.baseModelItemToMarketEntity
import com.example.e_marketapp.util.baseModelToMarketBasketEntity
import com.example.e_marketapp.util.clickWithDebounce
import com.example.e_marketapp.util.popBackStack
import com.example.e_marketapp.util.toastMessage
import com.example.e_marketapp.util.urlToImageGlide
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val args: DetailFragmentArgs by navArgs()
    private val marketDbViewModel: MarketDbViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkDbHasData()
        buttonClicks()
    }

    private fun checkDbHasData() {
        marketDbViewModel.getMealClickedItem(clickMarketId = args.itemArgs.id)
        viewLifecycleOwner.lifecycleScope.launch {
            //Burada ise yaşam döngüsü sahibinin yaşam döngüsünün kontrollerini sağlayabileceğimiz bir kısım gerçekleştirdim
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                marketDbViewModel.getAllData.collectLatest {
                    when (it.isDeleted) {
                        true -> {
                            binding.marketItemStar.visibility = View.INVISIBLE
                            binding.marketItemUnStar.visibility = View.VISIBLE
                        }

                        false -> {
                            binding.marketItemStar.visibility = View.VISIBLE
                            binding.marketItemUnStar.visibility = View.INVISIBLE
                        }

                        else -> {
                            binding.marketItemStar.visibility = View.INVISIBLE
                            binding.marketItemUnStar.visibility = View.VISIBLE
                        }
                    }

                    if (it.marketDataWithId != null) {
                        binding.marketItemStar.visibility = View.VISIBLE
                        binding.marketItemUnStar.visibility = View.INVISIBLE
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
                        args.itemArgs.apply {
                            setArgsData(
                                toolBarText = name,
                                imageUrl = image,
                                title = name,
                                description = description,
                                price = price
                            )
                        }
                    }
                }
            }

            //viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.DESTROYED){} sıralamsı önemli resume start gibi methodları sırayla yazmak gerekli.
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setArgsData(
        toolBarText: String,
        imageUrl: String,
        title: String,
        description: String,
        price: Int,
    ) {
        val customToolBarText = binding.customToolBar.findViewById<TextView>(R.id.toolbar_text)

        binding.apply {
            detailImage.urlToImageGlide(url = imageUrl)
            detailTitle.text = title
            detailDescription.text = description
            detailPriceAmount.text = price.toString()

            if (toolBarText.length > 15) {
                customToolBarText.text = toolBarText.substring(0, 15) + "..."
            } else {
                customToolBarText.text = toolBarText
            }
            customToolBar.navigationIconSetOnClickListener {
                popBackStack(requireView())
            }
        }
    }

    private fun buttonClicks() {
        binding.apply {
            addToCartButton.clickWithDebounce {
                clickAddToCardButton()
            }
            marketItemStar.setOnClickListener {
                toastMessage(context = requireContext(), message =  getString(R.string.removed_from_favorites))
                marketDbViewModel.deleteMarketItem(marketItemId = args.itemArgs.id)
            }

            marketItemUnStar.setOnClickListener {
                toastMessage(context = requireContext(), message =  getString(R.string.added_to_favorites))
                args.itemArgs.apply {
                    marketDbViewModel.addMarketItem(marketItem = baseModelItemToMarketEntity(args.itemArgs))
                }
            }
        }
    }

    private fun clickAddToCardButton() {
        marketDbViewModel.addBasketItem(basketEntity = baseModelToMarketBasketEntity(args.itemArgs))
        toastMessage(requireContext(), getString(R.string.item_added_basket))
    }
}
package com.example.e_marketapp.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.FavoriteAdapter
import com.example.e_marketapp.databinding.FragmentFavoriteBinding
import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.util.baseModelToMarketBasketEntity
import com.example.e_marketapp.util.toastMessage
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {

    private val marketDbViewModel: MarketDbViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeData()
    }

    private fun initAdapter() {
        adapter = FavoriteAdapter(
            addToCardClick = {
                marketDbViewModel.addBasketItem(baseModelToMarketBasketEntity(baseModelItem = it))
                toastMessage(context = requireContext(), getString(R.string.item_added_basket))
            },
            isStarred = {
                toastMessage(context = requireContext(), getString(R.string.removed_from_favorites))
                marketDbViewModel.deleteMarketItem(marketItemId = it)
            },
            clickItem = {
                val action = FavoriteFragmentDirections.actionFavoriteToDetailFragment(itemArgs = it)
                findNavController().navigate(action)
            }
        )
        binding.favoriteRecyclerView.adapter = adapter
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeData() {
        lifecycleScope.launch {
            marketDbViewModel.getAllData.collectLatest {
                binding.apply {
                    if (it.loading == true) {
                        progressBar.visibility = View.VISIBLE
                    } else if (it.error?.isNotBlank() == true) {
                        errorMessage.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                        errorMessage.text = it.error.toString()
                    } else if (it.marketData?.isNotEmpty() == true) {
                        errorMessage.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                        favoriteRecyclerView.visibility = View.VISIBLE
                        adapter.setFavoriteList(it.marketData)
                    } else {
                        errorMessage.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                        favoriteRecyclerView.visibility = View.INVISIBLE
                        errorMessage.text = getString(R.string.not_found_data_in_storage)
                    }
                }
            }
        }
    }
}
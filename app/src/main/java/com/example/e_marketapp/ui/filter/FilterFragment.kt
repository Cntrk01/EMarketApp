package com.example.e_marketapp.ui.filter

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.filter.BrandAdapter
import com.example.e_marketapp.adapter.filter.ModelAdapter
import com.example.e_marketapp.databinding.FragmentFilterBinding
import com.example.e_marketapp.model.FilterModelItem
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.util.toastMessage
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(FragmentFilterBinding::inflate) {
    private var oldCheckBoxValue = false
    private var newToOldCheckBoxValue = false
    private var priceHightToLowCheckBox = false
    private var priceLowToHightCheckBox = false

    private lateinit var brandAdapter: BrandAdapter
    private lateinit var modelAdapter: ModelAdapter

    private val brandFilter = FilterModelItem("", false)
    private val modelFilter = FilterModelItem("", false)

    private val marketDbViewModel: MarketDbViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sortByCheckBox()
        brandAdapter()
        modelAdapter()
        primaryButtonClick()
    }

    private fun primaryButtonClick() {
        binding.primaryButton.setOnClickListener {
            val sortText = when {
                oldCheckBoxValue -> getString(R.string.old_to_new)
                newToOldCheckBoxValue -> getString(R.string.new_to_old)
                priceHightToLowCheckBox -> getString(R.string.price_high_to_low)
                priceLowToHightCheckBox -> getString(R.string.price_low_to_high)
                else -> ""
            }
            val brandFilterText =
                brandFilter.title.takeIf { it.isNotEmpty() && brandFilter.checkBoxValue }
            val modelFilterText =
                modelFilter.title.takeIf { it.isNotEmpty() && modelFilter.checkBoxValue }

            if (sortText.isNotEmpty() || brandFilterText != null || modelFilterText != null) {
                marketDbViewModel.sortByText = sortText
                marketDbViewModel.brandFilter =
                    FilterModelItem(brandFilterText ?: "", brandFilter.checkBoxValue)
                marketDbViewModel.modelFilter =
                    FilterModelItem(modelFilterText ?: "", modelFilter.checkBoxValue)

                val action = FilterFragmentDirections.actionFilterFragmentToHome()
                findNavController().navigate(action)

            } else {
                toastMessage(
                    requireContext(),
                    getString(R.string.please_select_at_least_one_option)
                )
            }
        }
    }

    private fun sortByCheckBox() {
        binding.apply {
            oldToNewCheckBox.setOnClickListener {
                oldCheckBoxValue = !oldCheckBoxValue
                oldToNewCheckBox.isSelected = oldCheckBoxValue
            }
            newToOldCheckBox.setOnClickListener {
                newToOldCheckBoxValue = !newToOldCheckBoxValue
                newToOldCheckBox.isSelected = newToOldCheckBoxValue
            }
            priceHighToLowCheckBox.setOnClickListener {
                priceHightToLowCheckBox = !priceHightToLowCheckBox
                priceHighToLowCheckBox.isSelected = priceHightToLowCheckBox
            }
            priceLowToHighCheckBox.setOnClickListener {
                priceLowToHightCheckBox = !priceLowToHightCheckBox
                priceLowToHighCheckBox.isSelected = priceLowToHightCheckBox
            }
        }
    }

    private fun brandAdapter() {
        brandAdapter = BrandAdapter(selectedItem = {
            brandFilter.title = it.title
            brandFilter.checkBoxValue = it.checkBoxValue
        })
        binding.brandRecyclerView.adapter = brandAdapter
        binding.brandRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        brandAdapter.setBrandList(brandList())

        binding.brandSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filteredList = brandList().filter { it.title == query }
                if (filteredList.isNotEmpty()) {
                    binding.brandRecyclerView.visibility = View.VISIBLE
                    binding.brandErrorText.visibility = View.INVISIBLE

                    brandAdapter.setBrandList(filteredList)
                } else {
                    binding.brandRecyclerView.visibility = View.INVISIBLE
                    binding.brandErrorText.visibility = View.VISIBLE
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    binding.brandRecyclerView.visibility = View.VISIBLE
                    binding.brandErrorText.visibility = View.INVISIBLE
                    brandAdapter.setBrandList(brandList())
                }
                return false
            }
        })
    }

    private fun modelAdapter() {
        modelAdapter = ModelAdapter(selectedItem = {
            modelFilter.title = it.title
            modelFilter.checkBoxValue = it.checkBoxValue
        })
        binding.modelRecyclerView.adapter = modelAdapter
        binding.modelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        modelAdapter.setModelList(modelList())

        binding.modelSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filteredList = modelList().filter { it.title == query }
                if (filteredList.isNotEmpty()) {
                    binding.modelRecyclerView.visibility = View.VISIBLE
                    binding.modelErrorText.visibility = View.INVISIBLE

                    modelAdapter.setModelList(filteredList)
                } else {
                    binding.modelRecyclerView.visibility = View.INVISIBLE
                    binding.modelErrorText.visibility = View.VISIBLE
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    binding.modelRecyclerView.visibility = View.VISIBLE
                    binding.modelErrorText.visibility = View.INVISIBLE
                    modelAdapter.setModelList(modelList())
                }
                return false
            }
        })
    }

    //brand değişkenine göre filtrelenecek
    private fun brandList(): List<FilterModelItem> {
        return listOf(
            FilterModelItem("Lamborghini", false),
            FilterModelItem("Ferrari", false),
            FilterModelItem("Volkswagen", false),
            FilterModelItem("Smart", false),
            FilterModelItem("Fiat", false),
            FilterModelItem("Land Rover", false),
            FilterModelItem("Nissan", false),
            FilterModelItem("Bugatti", false),
            FilterModelItem("Tesla", false),

            )
    }

    private fun modelList(): List<FilterModelItem> {
        return listOf(
            FilterModelItem("CTS", false),
            FilterModelItem("Taurus", false),
            FilterModelItem("Jetta", false),
            FilterModelItem("Roadster", false),
            FilterModelItem("F-150", false),
            FilterModelItem("Corvette", false),
            FilterModelItem("Colorado", false),
            FilterModelItem("911", false),
        )
    }
}
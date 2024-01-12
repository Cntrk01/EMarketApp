package com.example.e_marketapp.ui.filter

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.filter.BrandAdapter
import com.example.e_marketapp.adapter.filter.ModelAdapter
import com.example.e_marketapp.adapter.filter.SortByAdapter
import com.example.e_marketapp.databinding.FragmentFilterBinding
import com.example.e_marketapp.model.FilterModelItem
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.util.toastMessage
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(FragmentFilterBinding::inflate) {
    private var sortByItemCheck = false

    private lateinit var brandAdapter: BrandAdapter
    private lateinit var modelAdapter: ModelAdapter
    private lateinit var sortByAdapter: SortByAdapter

    private val brandFilter = FilterModelItem("", false)
    private val modelFilter = FilterModelItem("", false)

    private val marketDbViewModel: MarketDbViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brandAdapter()
        modelAdapter()
        primaryButtonClick()
        sortByAdapter()
        actionFragment()
    }
    private fun actionFragment(){
        binding.customToolBar2.navigationIconSetOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun primaryButtonClick() {
        binding.primaryButton.setOnClickListener {
            val sortText = if (sortByItemCheck){
                marketDbViewModel.sortByText
            } else {
                marketDbViewModel.sortByText==""
            }
            val brandFilterText = brandFilter.title.takeIf { it.isNotEmpty() && brandFilter.checkBoxValue }
            val modelFilterText = modelFilter.title.takeIf { it.isNotEmpty() && modelFilter.checkBoxValue }

            if (sortText.toString().isNotBlank() || brandFilterText != null || modelFilterText != null) {
                marketDbViewModel.sortByText = sortText.toString()
                marketDbViewModel.brandFilter = FilterModelItem(brandFilterText ?: "", brandFilter.checkBoxValue)
                marketDbViewModel.modelFilter = FilterModelItem(modelFilterText ?: "", modelFilter.checkBoxValue)
                val action = FilterFragmentDirections.actionFilterFragmentToHome()
                findNavController().navigate(action)
            } else {
                toastMessage(requireContext(), getString(R.string.please_select_at_least_one_option))
            }
        }
    }

    private fun sortByAdapter(){
       sortByAdapter=SortByAdapter(selectedItem = {
           marketDbViewModel.sortByText=it.title
           sortByItemCheck=it.checkBoxValue
       })
       binding.sortByRecyclerView.adapter=sortByAdapter
       binding.sortByRecyclerView.layoutManager=LinearLayoutManager(requireContext())
       sortByAdapter.setModelList(sortList())
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

                    modelAdapter.setModelList(list=filteredList)
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
                    modelAdapter.setModelList(list=modelList())
                }
                return false
            }
        })
    }

    private fun brandList(): List<FilterModelItem> {
        return listOf(
            FilterModelItem(title = getString(R.string.lamborghini), checkBoxValue = false),
            FilterModelItem(title = getString(R.string.ferrari), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.volkswagen), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.smart), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.fiat),checkBoxValue = false),
            FilterModelItem(title = getString(R.string.land_rover), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.nissan), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.bugatti), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.tesla), checkBoxValue =false),
        )
    }

    private fun modelList(): List<FilterModelItem> {
        return listOf(
            FilterModelItem(title = getString(R.string.cts), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.taurus), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.jetta), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.roadster), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.f_150), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.corvette), checkBoxValue =false),
            FilterModelItem(title = getString(R.string.colorado), checkBoxValue =false),
            FilterModelItem(title = getString(R.string._911), checkBoxValue =false),
        )
    }

    private fun sortList() : List<FilterModelItem>{
        return listOf(
            FilterModelItem(getString(R.string.old_to_new), false),
            FilterModelItem(getString(R.string.new_to_old), false),
            FilterModelItem(getString(R.string.price_high_to_low), false),
            FilterModelItem(getString(R.string.price_low_to_high), false)
        )
    }
}
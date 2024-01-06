package com.example.e_marketapp.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.ProfileAdapter
import com.example.e_marketapp.databinding.FragmentProfileBinding
import com.example.e_marketapp.util.BaseFragment
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate){
    private val marketDbViewModel : MarketDbViewModel by viewModels()
    private lateinit var profileAdapter: ProfileAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeData()
    }

    private fun observeData(){
        binding.apply {
           marketDbViewModel.apply {
               lifecycleScope.launch {
                   getHistoryOrder ()
                   basketState.collectLatest {
                       if (it.loading==true){
                           progressBar.visibility=View.VISIBLE
                       }
                       else if (it.error?.isNotEmpty() == true){
                           progressBar.visibility=View.INVISIBLE
                           errorText.visibility=View.VISIBLE
                           errorText.text=it.error
                       }
                       else if (it.historyOrderModel?.isNotEmpty() == true){
                           progressBar.visibility=View.INVISIBLE
                           errorText.visibility=View.INVISIBLE
                           historyRecyclerView.visibility=View.VISIBLE
                           profileAdapter.setHistoryList(it.historyOrderModel)
                       }
                       else{
                           progressBar.visibility=View.INVISIBLE
                           errorText.visibility=View.VISIBLE
                           historyRecyclerView.visibility=View.INVISIBLE
                           errorText.text=getString(R.string.not_found_history)
                       }
                   }
               }
           }
        }

    }

    private fun initAdapter(){
        profileAdapter= ProfileAdapter(clickHistoryItem = {
            val action = ProfileFragmentDirections.actionProfileToHistoryDetailFragment(it)
            findNavController().navigate(action)
        })
        binding.historyRecyclerView.adapter=profileAdapter
        binding.historyRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    }
}
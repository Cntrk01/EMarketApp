package com.example.e_marketapp.ui.profile.detail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_marketapp.R
import com.example.e_marketapp.adapter.history.HistoryListAdapter
import com.example.e_marketapp.databinding.FragmentHistoryDetailBinding
import com.example.e_marketapp.util.BaseFragment

class HistoryDetailFragment : BaseFragment<FragmentHistoryDetailBinding>(FragmentHistoryDetailBinding::inflate) {

    private val args : HistoryDetailFragmentArgs by navArgs()
    private lateinit var historyAdapter : HistoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backClickButton()
        initAdapter()
        setToolBarText()
    }
    private fun initAdapter(){
        historyAdapter= HistoryListAdapter()
        binding.historyDetailRecyclerView.adapter=historyAdapter
        binding.historyDetailRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        args.history.historyList?.let { historyAdapter.setHistoryList(it) }
    }

    private fun backClickButton(){
        binding.customToolBar.navigationIconSetOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setToolBarText(){
        val customToolBarText = binding.customToolBar.findViewById<TextView>(R.id.toolbar_text)
        customToolBarText.text=getString(R.string.history)
    }
}
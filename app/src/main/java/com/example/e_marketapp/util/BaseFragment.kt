package com.example.e_marketapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

//Burada binding olarak verilen fragmentin bindingini alarak
// inflater oluşturarak gelen viewin görünüm değişkenlerini invok ederek görünümü oluşturur.
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>,
) : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }
}
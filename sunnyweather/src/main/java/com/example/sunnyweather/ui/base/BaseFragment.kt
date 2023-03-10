package com.example.sunnyweather.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.sunnyweather.databinding.FragmentPlaceBinding


abstract class BaseFragment<T:ViewBinding>:Fragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = onCreateViewBinding(inflater,container)
        return binding.root
    }

    abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup?): T

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
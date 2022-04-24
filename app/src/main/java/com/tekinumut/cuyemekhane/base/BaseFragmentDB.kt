package com.tekinumut.cuyemekhane.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragmentDB<DB : ViewDataBinding>(private val resourceId: Int) : Fragment() {

    lateinit var binding: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, resourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
package com.tekinumut.cuyemekhane.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.ComponentListAdapter
import com.tekinumut.cuyemekhane.base.BaseFragmentVB
import com.tekinumut.cuyemekhane.databinding.FragmentDmComponentBinding
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel

class ComponentFragment : BaseFragmentVB<FragmentDmComponentBinding>(
    FragmentDmComponentBinding::inflate
) {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        arguments?.getString(ConstantsGeneral.bundleImgKey)?.let {
            binding.ivComponent.setImageBitmap(Utility.base64ToString(it))
        }

        arguments?.getStringArrayList(ConstantsGeneral.bundleComponentListKey)?.let {
            binding.recyclerComponent.adapter = ComponentListAdapter(it)
        }

        arguments?.getString(ConstantsGeneral.bundleFoodName)?.let {
            mainViewModel.updateActionTitle(it)
        }
    }

    private fun init() {
        with(binding.recyclerComponent) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }
}
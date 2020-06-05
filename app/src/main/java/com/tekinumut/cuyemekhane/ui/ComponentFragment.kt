package com.tekinumut.cuyemekhane.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.MainViewModel
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.ComponentListAdapter
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.Utility
import kotlinx.android.synthetic.main.fragment_dm_component.*

class ComponentFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerComponent: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dm_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        arguments?.getString(ConstantsGeneral.bundleImgKey)?.let {
            iv_component.setImageBitmap(Utility.base64ToString(it))
        }

        arguments?.getStringArrayList(ConstantsGeneral.bundleComponentListKey)?.let {
            recyclerComponent.adapter = ComponentListAdapter(it)
        }

        arguments?.getString(ConstantsGeneral.bundleFoodName)?.let {
            mainViewModel.updateActionTitle(it)
        }

    }

    private fun init(view: View) {
        recyclerComponent = view.findViewById(R.id.recyclerComponent)
        recyclerComponent.layoutManager = LinearLayoutManager(context)
        recyclerComponent.setHasFixedSize(true)
        recyclerComponent.addItemDecoration(DividerItemDecoration(recyclerComponent.context, DividerItemDecoration.VERTICAL))


    }

}
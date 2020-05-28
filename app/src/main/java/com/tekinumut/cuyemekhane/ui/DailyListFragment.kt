package com.tekinumut.cuyemekhane.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tekinumut.cuyemekhane.MainViewModel
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.DailyListAdapter
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.library.Utility

class DailyListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var recyclerList: RecyclerView
    private lateinit var mainPref: MainPref
    private val loadingDialog by lazy { Utility.getLoadingDialog(requireActivity()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainPref = MainPref.getInstance(requireContext())
        val root = inflater.inflate(R.layout.fragment_daily_list, container, false)
        recyclerList = root.findViewById(R.id.recyclerDailyList)
        refreshLayout = root.findViewById(R.id.refreshDailyList)

        getFoodData()

        refreshLayout.setOnRefreshListener(this)
        recyclerList.layoutManager = LinearLayoutManager(context)
        recyclerList.setHasFixedSize(true)

        mainViewModel.getDailyList.observe(requireActivity(), Observer { dateWithAll ->
            dateWithAll?.let {
                mainViewModel.updateActionTitle(it.foodDate.name)
                recyclerList.adapter = DailyListAdapter(it.yemekWithComponentComp)
            }
        })

        return root
    }

    private fun getFoodData() {
        // Daha önce çağrılmamışsa çağır
        val isWorkedBefore = mainPref.getBoolean(ConstantsGeneral.prefCheckDailyListUpdated, false)

        if (!isWorkedBefore) {
            mainViewModel.getFoodData(ConstantsGeneral.dbNameDaily).observe(requireActivity(), Observer {
                when (it) {
                    Resource.InProgress -> loadingDialog.show()
                    is Resource.Success -> {
                        Toast.makeText(context, getString(R.string.data_loaded), Toast.LENGTH_SHORT).show()
                        onSuccessAndError()
                    }
                    is Resource.Error -> {
                        val message = "${getString(R.string.error_loading_data)} \nHata sebebi: ${it.exception.message}"
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        onSuccessAndError()
                    }
                }
            })
        }
    }

    private fun onSuccessAndError() {
        mainPref.save(ConstantsGeneral.prefCheckDailyListUpdated, true)
        loadingDialog.dismiss()
        refreshLayout.isRefreshing = false
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
        refreshLayout.isRefreshing = false
    }

    override fun onRefresh() {
        refreshLayout.post { getFoodData() }
    }

}
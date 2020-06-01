package com.tekinumut.cuyemekhane.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.DuyurularAdapter
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_duyurular.*


class DuyurularFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val loadingDialog by lazy { Utility.getLoadingDialog(requireActivity()) }
    private lateinit var recyclerDuyurular: RecyclerView
    private lateinit var refreshDuyurular: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_duyurular, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        observeDuyurularDB()
    }

    private fun init(view: View) {
        recyclerDuyurular = view.findViewById(R.id.recyclerDuyurular)
        refreshDuyurular = view.findViewById(R.id.srlDuyurular)
        refreshDuyurular.setOnRefreshListener(this)
        recyclerDuyurular.layoutManager = LinearLayoutManager(context)
        recyclerDuyurular.setHasFixedSize(true)
    }

    private fun observeDuyurularDB() {
        mainViewModel.getDuyurular.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                recyclerDuyurular.adapter = DuyurularAdapter(it)
                activeLayout(false)
            } else {
                activeLayout(true)
            }
        })
    }

    /**
     * Gösterilecek ekranı düzenler
     */
    private fun activeLayout(isEmpty: Boolean) {
        if (isEmpty) {
            llEmptyDuyurular.visibility = View.VISIBLE
            recyclerDuyurular.visibility = View.GONE
        } else {
            llEmptyDuyurular.visibility = View.GONE
            recyclerDuyurular.visibility = View.VISIBLE
        }
    }

    /**
     * Duyuruları web sitesinen al
     */
    private fun getDuyurularData() {
        mainViewModel.getDuyurularData().observe(viewLifecycleOwner, Observer {
            when (it) {
                Resource.InProgress -> loadingDialog.show()
                is Resource.Success -> {
                    Toast.makeText(context, getString(R.string.duyurular_loaded), Toast.LENGTH_SHORT).show()
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

    private fun onSuccessAndError() {
        loadingDialog.dismiss()
        refreshDuyurular.isRefreshing = false
    }

    override fun onRefresh() {
        refreshDuyurular.post { getDuyurularData() }
    }
}
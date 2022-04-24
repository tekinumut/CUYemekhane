package com.tekinumut.cuyemekhane.ui.draweritems.duyurular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.DuyurularAdapter
import com.tekinumut.cuyemekhane.base.BaseFragmentVB
import com.tekinumut.cuyemekhane.databinding.FragmentDuyurularBinding
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.library.Utility


class DuyurularFragment : BaseFragmentVB<FragmentDuyurularBinding>(
    FragmentDuyurularBinding::inflate
), SwipeRefreshLayout.OnRefreshListener {

    private val duyurularViewModel: DuyurularViewModel by viewModels()
    private val loadingDialog by lazy { Utility.getLoadingDialog(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeDuyurularDB()
        // Sunucudan güncel duyuruları çek
        getDuyurularData(false)
    }

    private fun init() {
        binding.refreshDuyurular.setOnRefreshListener(this)
        binding.recyclerDuyurular.layoutManager = LinearLayoutManager(context)
        binding.recyclerDuyurular.setHasFixedSize(true)
    }

    /**
     * Duyurular veritabanını izle
     */
    private fun observeDuyurularDB() {
        duyurularViewModel.getDuyurular.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.recyclerDuyurular.adapter = DuyurularAdapter(it)
                activeLayout(false)
            } else {
                activeLayout(true)
            }
        }
    }

    /**
     * Gösterilecek ekranı düzenler
     */
    private fun activeLayout(isEmpty: Boolean) {
        if (isEmpty) {
            binding.llEmptyDuyurular.visibility = View.VISIBLE
            binding.recyclerDuyurular.visibility = View.GONE
        } else {
            binding.llEmptyDuyurular.visibility = View.GONE
            binding.recyclerDuyurular.visibility = View.VISIBLE
        }
    }

    /**
     * Duyuruları web sitesinen al
     * @param isSwipeRefresh Kullanıcı kendisi mi yenilemek istiyor.
     */
    private fun getDuyurularData(isSwipeRefresh: Boolean) {
        val mainPref = MainPref.getInstance(requireContext())
        if (shouldAutoRefreshData(isSwipeRefresh, mainPref)) {
            duyurularViewModel.getDuyurularData().observe(viewLifecycleOwner) {
                when (it) {
                    Resource.InProgress -> if (!isSwipeRefresh) loadingDialog.show()
                    is Resource.Success -> {
                        // Uygulama bir kez güncellendi. Otomatik güncellemeyi kapat.
                        mainPref.save(ConstantsGeneral.prefCheckDuyurularWorkedBefore, true)
                        onSuccessAndError(getString(R.string.duyurular_loaded))
                    }
                    is Resource.Error -> {
                        val message =
                            "${getString(R.string.error_loading_data)} \nHata sebebi: ${it.exception.message}"
                        onSuccessAndError(message)
                    }
                }
            }
        }
    }

    private fun onSuccessAndError(message: String) {
        loadingDialog.dismiss()
        binding.refreshDuyurular.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Metodun çalışma biçimi DailyListFragment sınıfındaki eş metotta açıklanmaktadır.
     */
    private fun shouldAutoRefreshData(isSwipeRefresh: Boolean, mainPref: MainPref): Boolean {
        val autoUpdateVal = mainPref.getBoolean(
            ConstantsGeneral.prefDuyurularAutoUpdateKey,
            ConstantsGeneral.defValDuyurularAutoUpdate
        )
        val isWorkedBefore =
            mainPref.getBoolean(ConstantsGeneral.prefCheckDuyurularWorkedBefore, false)
        // Otomatik güncelleme olduğu durumda oluşacak sonucu değere ata
        var autoUpdateResult = if (autoUpdateVal) !isWorkedBefore else false
        // Eğer kullanıcı kendi yenilemek istediyse direk true yap
        if (isSwipeRefresh) autoUpdateResult = true
        return autoUpdateResult
    }

    override fun onRefresh() {
        binding.refreshDuyurular.post { getDuyurularData(true) }
    }
}
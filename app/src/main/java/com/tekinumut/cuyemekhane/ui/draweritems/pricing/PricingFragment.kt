package com.tekinumut.cuyemekhane.ui.draweritems.pricing

import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseFragmentVB
import com.tekinumut.cuyemekhane.databinding.FragmentPricingBinding
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.library.Utility

class PricingFragment : BaseFragmentVB<FragmentPricingBinding>(FragmentPricingBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener {

    private val pricingViewModel: PricingViewModel by viewModels()
    private val loadingDialog by lazy { Utility.getLoadingDialog(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refreshPricing.setOnRefreshListener(this)
        disableRefreshIfWebScrollOnBottom()
        observePricingTable()
        getPricingData(false)
    }

    private fun observePricingTable() {
        pricingViewModel.getPricing.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                activeLayout(true)
            } else {
                val encodedHtml = Base64.encodeToString(it.toByteArray(), Base64.NO_PADDING)
                binding.webViewPricing.loadData(encodedHtml, "text/html", "base64")
                activeLayout(false)
            }
        }
    }

    private fun getPricingData(isSwipeRefresh: Boolean) {
        val mainPref = MainPref.getInstance(requireContext())
        if (shouldAutoRefreshData(isSwipeRefresh, mainPref)) {
            pricingViewModel.getPricingData().observe(viewLifecycleOwner) {
                when (it) {
                    Resource.InProgress -> if (!isSwipeRefresh) loadingDialog.show()
                    is Resource.Success -> {
                        mainPref.save(ConstantsGeneral.prefCheckDailyListWorkedBefore, true)
                        onSuccessAndError(getString(R.string.pricing_loaded))
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
        binding.refreshPricing.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Gösterilecek ekranı düzenler
     */
    private fun activeLayout(isEmpty: Boolean) {
        if (isEmpty) {
            binding.llEmptyPricing.visibility = View.VISIBLE
            binding.webViewPricing.visibility = View.GONE
        } else {
            binding.llEmptyPricing.visibility = View.GONE
            binding.webViewPricing.visibility = View.VISIBLE
        }
    }

    /**
     * Metodun çalışma biçimi DailyListFragment sınıfındaki eş metotta açıklanmaktadır.
     */
    private fun shouldAutoRefreshData(isSwipeRefresh: Boolean, mainPref: MainPref): Boolean {
        val autoUpdateVal = mainPref.getBoolean(
            ConstantsGeneral.prefPricingAutoUpdateKey,
            ConstantsGeneral.defValPricingAutoUpdate
        )
        val isWorkedBefore =
            mainPref.getBoolean(ConstantsGeneral.prefCheckPricingWorkedBefore, false)
        // Otomatik güncelleme olduğu durumda oluşacak sonucu değere ata
        var autoUpdateResult = if (autoUpdateVal) !isWorkedBefore else false
        // Eğer kullanıcı kendi yenilemek istediyse direk true yap
        if (isSwipeRefresh) autoUpdateResult = true
        return autoUpdateResult
    }

    /**
     * Eğer webView Scroll'u aşağıda ise kullanıcı ekranı yukarı kaydırırken
     * SwipeRefresh ile çakışmaması için SwipeRefreshi iptal et
     */
    private fun disableRefreshIfWebScrollOnBottom() {
        // Eğer scroll en üstte değilse refresh yapmayı engelle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.webViewPricing.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                binding.refreshPricing.isEnabled = scrollY == 0
            }
        }
    }

    override fun onRefresh() {
        binding.refreshPricing.post { getPricingData(true) }
    }
}
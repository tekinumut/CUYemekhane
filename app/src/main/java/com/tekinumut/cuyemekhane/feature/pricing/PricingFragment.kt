package com.tekinumut.cuyemekhane.feature.pricing

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseFragment
import com.tekinumut.cuyemekhane.common.extensions.collectWithLifecycle
import com.tekinumut.cuyemekhane.common.extensions.hide
import com.tekinumut.cuyemekhane.common.extensions.loadBase64Data
import com.tekinumut.cuyemekhane.common.extensions.orEmptyString
import com.tekinumut.cuyemekhane.common.extensions.setupToolbar
import com.tekinumut.cuyemekhane.common.extensions.show
import com.tekinumut.cuyemekhane.common.helpers.CuAnimationHelper.rotateClockWise
import com.tekinumut.cuyemekhane.common.ui.CuToolbar
import com.tekinumut.cuyemekhane.databinding.PricingFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PricingFragment : BaseFragment<PricingFragmentBinding>(PricingFragmentBinding::inflate) {

    private val viewModel by viewModels<PricingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObservers()
    }

    private fun initUI() {
        with(binding) {
            setupToolbar(
                toolbar,
                CuToolbar.ConfigModel(
                    displayNavIcon = true,
                    actionIcon = R.drawable.ic_refresh,
                    navIconListener = { findNavController().popBackStack() },
                    actionButtonListener = {
                        it.rotateClockWise()
                        viewModel.getPricingInfo()
                    },
                    mainTitle = findNavController().currentDestination?.label.orEmptyString()
                )
            )
            viewCommonError.setData(viewModel.noPricingDataErrorModel)
            viewCommonError.setPositiveListener {
                viewModel.getPricingInfo()
            }
        }
    }

    private fun initObservers() {
        collectWithLifecycle(viewModel.uiState) { uiState ->
            when (uiState.state) {
                PricingViewModel.State.Initial -> Unit
                is PricingViewModel.State.PricingDataFetched -> {
                    with(binding) {
                        viewCommonError.hide()
                        webViewPricing.show()
                        webViewPricing.loadBase64Data(uiState.state.htmlSource)
                    }
                }
                PricingViewModel.State.NoPricingDataFound -> {
                    with(binding) {
                        webViewPricing.hide()
                        viewCommonError.show()
                    }
                }
            }
            with(binding) {
                if (uiState.isLoading) {
                    progressLoading.isVisible = true
                    viewCommonError.hide()
                } else {
                    progressLoading.isVisible = false
                    toolbar.getActionButton.clearAnimation()
                    progressLoading.hide()
                }
            }
        }
    }
}
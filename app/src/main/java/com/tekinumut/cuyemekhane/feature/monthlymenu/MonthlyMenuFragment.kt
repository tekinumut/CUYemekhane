package com.tekinumut.cuyemekhane.feature.monthlymenu

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseFragment
import com.tekinumut.cuyemekhane.common.extensions.collectWithLifecycle
import com.tekinumut.cuyemekhane.common.extensions.hide
import com.tekinumut.cuyemekhane.common.extensions.navigateToMonthlyMenuDetail
import com.tekinumut.cuyemekhane.common.extensions.setupToolbar
import com.tekinumut.cuyemekhane.common.extensions.show
import com.tekinumut.cuyemekhane.common.helpers.CuAnimationHelper.rotateClockWise
import com.tekinumut.cuyemekhane.common.ui.CuToolbar
import com.tekinumut.cuyemekhane.databinding.FragmentMonthlyMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyMenuFragment : BaseFragment<FragmentMonthlyMenuBinding>(
    FragmentMonthlyMenuBinding::inflate
) {
    private val viewModel by viewModels<MonthlyMenuViewModel>()

    private val monthlyMenuAdapter = MonthlyMenuAdapter { dailyMenu ->
        findNavController().navigateToMonthlyMenuDetail(dailyMenu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObservers()
    }

    private fun initUI() {
        with(binding) {
            setupToolbar(
                toolbar = toolbar,
                config = CuToolbar.ConfigModel(
                    actionIcon = R.drawable.ic_refresh,
                    actionButtonListener = {
                        it.rotateClockWise()
                        viewModel.fetchMonthlyMenu()
                    }
                )
            )
            swipeRefreshLayoutRoot.setOnRefreshListener {
                viewModel.fetchMonthlyMenu()
            }
            recyclerFoods.adapter = monthlyMenuAdapter
            viewCommonError.setData(viewModel.emptyListErrorData)
            viewCommonError.setPositiveListener {
                viewModel.fetchMonthlyMenu()
            }
        }
    }

    private fun initObservers() {
        collectWithLifecycle(viewModel.uiState) { uiState ->
            when (uiState.state) {
                MonthlyMenuViewModel.State.Initial -> Unit
                is MonthlyMenuViewModel.State.MenuFetched -> {
                    with(binding) {
                        viewCommonError.hide()
                        recyclerFoods.show()
                    }
                    monthlyMenuAdapter.submitList(uiState.state.menu.dailyMenuList)
                }
                MonthlyMenuViewModel.State.NoMenuFound -> {
                    with(binding) {
                        recyclerFoods.hide()
                        viewCommonError.show()
                    }
                }
            }
        }
        collectWithLifecycle(viewModel.event) { event ->
            when (event) {
                MonthlyMenuViewModel.Event.ShowLoading -> {
                    with(binding) {
                        progressLoading.isGone = swipeRefreshLayoutRoot.isRefreshing
                        if (viewCommonError.isVisible) {
                            viewCommonError.hide()
                        }
                    }
                }
                MonthlyMenuViewModel.Event.HideLoading -> {
                    with(binding) {
                        toolbar.getActionButton.clearAnimation()
                        progressLoading.hide()
                        swipeRefreshLayoutRoot.isRefreshing = false
                    }
                }
            }
        }
    }
}
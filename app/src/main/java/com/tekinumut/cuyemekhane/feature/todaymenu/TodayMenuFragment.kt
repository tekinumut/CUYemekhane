package com.tekinumut.cuyemekhane.feature.todaymenu

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.tekinumut.cuyemekhane.base.BaseFragment
import com.tekinumut.cuyemekhane.common.extensions.collectWithLifecycle
import com.tekinumut.cuyemekhane.common.extensions.hide
import com.tekinumut.cuyemekhane.common.extensions.setupToolbar
import com.tekinumut.cuyemekhane.common.extensions.show
import com.tekinumut.cuyemekhane.common.ui.CommonErrorModel
import com.tekinumut.cuyemekhane.common.ui.FullScreenImageDialog
import com.tekinumut.cuyemekhane.common.util.Constants
import com.tekinumut.cuyemekhane.common.util.Utility
import com.tekinumut.cuyemekhane.databinding.FragmentTodayMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayMenuFragment : BaseFragment<FragmentTodayMenuBinding>(
    FragmentTodayMenuBinding::inflate
) {
    private val viewModel by viewModels<TodayMenuViewModel>()

    private val todayMenuAdapter = TodayMenuAdapter { imageUrl ->
        FullScreenImageDialog
            .newInstance(imageUrl)
            .show(childFragmentManager, FullScreenImageDialog.TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() {
        with(binding) {
            setupToolbar(toolbar = toolbar)
            swipeRefreshLayoutRoot.setOnRefreshListener {
                viewModel.fetchDailyMenu()
            }
            recyclerFoods.adapter = todayMenuAdapter
            viewCommonError.setData(CommonErrorModel.menuNotFoundError)
            viewCommonError.setPositiveListener {
                Utility.openWebSiteWithCustomTabs(it.context, Constants.NETWORK.MAIN_PAGE)
            }
        }
    }

    private fun initObservers() {
        collectWithLifecycle(viewModel.uiState) { uiState ->
            when (uiState.state) {
                TodayMenuViewModel.State.Initial -> Unit
                is TodayMenuViewModel.State.MenuFetched -> {
                    with(binding) {
                        viewCommonError.hide()
                        recyclerFoods.show()
                    }
                    todayMenuAdapter.submitList(uiState.state.menu.foods)
                }
                is TodayMenuViewModel.State.NoMenuFound -> {
                    with(binding) {
                        recyclerFoods.hide()
                        viewCommonError.show()
                    }
                }
            }
        }
        collectWithLifecycle(viewModel.event) { event ->
            when (event) {
                TodayMenuViewModel.Event.ShowLoading -> {
                    with(binding) {
                        progressLoading.isGone = swipeRefreshLayoutRoot.isRefreshing

                    }
                }
                TodayMenuViewModel.Event.HideLoading -> {
                    binding.progressLoading.hide()
                    binding.swipeRefreshLayoutRoot.isRefreshing = false
                }
            }
        }
    }
}
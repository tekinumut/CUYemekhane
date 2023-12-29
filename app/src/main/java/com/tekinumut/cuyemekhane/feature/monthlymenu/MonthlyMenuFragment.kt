package com.tekinumut.cuyemekhane.feature.monthlymenu

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.tekinumut.cuyemekhane.base.BaseFragment
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyMenuUIModel
import com.tekinumut.cuyemekhane.common.extensions.collectWithLifecycle
import com.tekinumut.cuyemekhane.common.extensions.hide
import com.tekinumut.cuyemekhane.common.extensions.show
import com.tekinumut.cuyemekhane.databinding.FragmentMonthlyMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyMenuFragment : BaseFragment<FragmentMonthlyMenuBinding>(
    FragmentMonthlyMenuBinding::inflate
) {
    private val viewModel by viewModels<MonthlyMenuViewModel>()

    private val monthlyMenuAdapter = MonthlyMenuAdapter { dailyMenu ->
        navigateToMonthlyMenuDetail(dailyMenu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObservers()
    }

    private fun initUI() {
        with(binding) {
            swipeRefreshLayoutRoot.setOnRefreshListener {
                viewModel.fetchMonthlyMenu()
            }
            recyclerFoods.adapter = monthlyMenuAdapter
        }
    }

    private fun initObservers() {
        collectWithLifecycle(viewModel.uiState) { uiState ->
            when (uiState.state) {
                MonthlyMenuViewModel.State.Initial -> Unit
                is MonthlyMenuViewModel.State.MenuFetched -> {
                    with(binding) {
                        //   includeErrorLayout.root.hide()
                        recyclerFoods.show()
                    }
                    monthlyMenuAdapter.submitList(uiState.state.menu.dailyMenuList)
                }
                MonthlyMenuViewModel.State.NoMenuFound -> {
                    with(binding) {
                        recyclerFoods.hide()
                        //  includeErrorLayout.root.show()
                    }
                }
            }
        }
        collectWithLifecycle(viewModel.event) { event ->
            when (event) {
                MonthlyMenuViewModel.Event.ShowLoading -> {
                    with(binding) {
                        progressLoading.isGone = swipeRefreshLayoutRoot.isRefreshing
                    }
                }
                MonthlyMenuViewModel.Event.HideLoading -> {
                    binding.progressLoading.hide()
                    binding.swipeRefreshLayoutRoot.isRefreshing = false
                }
            }
        }
    }

    private fun navigateToMonthlyMenuDetail(dailyMenuUIModel: DailyMenuUIModel) {
        val action = MonthlyMenuFragmentDirections.actionToMonthlyMenuDetailBottomSheet(
            dailyMenuUIModel
        )
        view?.findNavController()?.navigate(action)
    }
}
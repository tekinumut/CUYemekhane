package com.tekinumut.cuyemekhane.feature.monthlymenudetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.tekinumut.cuyemekhane.base.BaseBottomSheetFragment
import com.tekinumut.cuyemekhane.common.extensions.collectWithLifecycle
import com.tekinumut.cuyemekhane.common.extensions.setTextOrHide
import com.tekinumut.cuyemekhane.common.extensions.show
import com.tekinumut.cuyemekhane.databinding.BottomSheetMonthlyMenuDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyMenuDetailBottomSheet : BaseBottomSheetFragment<BottomSheetMonthlyMenuDetailBinding>(
    BottomSheetMonthlyMenuDetailBinding::inflate
) {
    private val args: MonthlyMenuDetailBottomSheetArgs by navArgs()
    private val viewModel by viewModels<MonthlyMenuDetailViewModel>()

    private val menuDetailAdapter = MonthlyMenuDetailAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateDailyMenuList(args.dailyMenu?.foodList.orEmpty())
        initUI()
        initObservers()
    }

    private fun initUI() {
        with(binding) {
            textDate.setTextOrHide(args.dailyMenu?.date)
            recyclerFoods.adapter = menuDetailAdapter
        }
    }

    private fun initObservers() {
        collectWithLifecycle(viewModel.uiState) { uiState ->
            with(binding) {
                when (uiState.state) {
                    MonthlyMenuDetailViewModel.State.Initial -> Unit
                    is MonthlyMenuDetailViewModel.State.MenuFetched -> {
                        menuDetailAdapter.submitList(uiState.menu)
                        recyclerFoods.show()
                    }
                }
            }
        }
    }
}
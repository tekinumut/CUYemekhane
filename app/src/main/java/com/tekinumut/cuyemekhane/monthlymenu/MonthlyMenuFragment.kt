package com.tekinumut.cuyemekhane.monthlymenu

import androidx.fragment.app.viewModels
import com.tekinumut.cuyemekhane.common.extensions.BaseFragment
import com.tekinumut.cuyemekhane.databinding.FragmentMonthlyMenuBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Umut Tekin on 16.01.2023.
 */
@AndroidEntryPoint
class MonthlyMenuFragment : BaseFragment<FragmentMonthlyMenuBinding>(
    FragmentMonthlyMenuBinding::inflate
) {
    private val viewModel by viewModels<MonthlyMenuViewModel>()
}
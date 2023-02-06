package com.tekinumut.cuyemekhane.todaymenu

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tekinumut.cuyemekhane.common.extensions.BaseFragment
import com.tekinumut.cuyemekhane.common.extensions.hide
import com.tekinumut.cuyemekhane.common.extensions.show
import com.tekinumut.cuyemekhane.common.util.Constants
import com.tekinumut.cuyemekhane.common.util.Utility
import com.tekinumut.cuyemekhane.databinding.FragmentTodayMenuBinding
import com.tekinumut.cuyemekhane.todaymenu.events.TodayMenuEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by Umut Tekin on 16.01.2023.
 */
@AndroidEntryPoint
class TodayMenuFragment : BaseFragment<FragmentTodayMenuBinding>(
    FragmentTodayMenuBinding::inflate
) {

    private val viewModel by viewModels<TodayMenuViewModel>()

    private val todayMenuAdapter = TodayMenuAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() {
        with(binding) {
            binding.swipeRefreshLayoutRoot.setOnRefreshListener(refreshListener)
            recyclerFoods.adapter = todayMenuAdapter
            includeErrorLayout.buttonOpenWebsite.setOnClickListener {
                Utility.openWebSiteWithCustomTabs(it.context, Constants.NETWORK.MAIN_PAGE)
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.todayMenuEvent.collect { mainPageEvent ->
                    when (mainPageEvent) {
                        TodayMenuEvent.Default -> Unit
                        TodayMenuEvent.Loading -> {
                            with(binding) {
                                includeErrorLayout.root.hide()
                                recyclerFoods.hide()
                                progressLoading.isGone = swipeRefreshLayoutRoot.isRefreshing
                            }
                        }
                        is TodayMenuEvent.Failure -> {
                            with(binding) {
                                progressLoading.hide()
                                recyclerFoods.hide()
                                stopRefreshListener()
                                includeErrorLayout.root.show()
                            }
                        }
                        is TodayMenuEvent.Success -> {
                            with(binding) {
                                includeErrorLayout.root.hide()
                                progressLoading.hide()
                                recyclerFoods.show()
                            }
                            stopRefreshListener()
                            todayMenuAdapter.submitList(mainPageEvent.todayMenuUIModel.foods)
                        }
                    }
                }
            }
        }
    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        binding.swipeRefreshLayoutRoot.post {
            viewModel.fetchMainPage()
        }
    }

    private fun stopRefreshListener() {
        binding.swipeRefreshLayoutRoot.isRefreshing = false
    }
}
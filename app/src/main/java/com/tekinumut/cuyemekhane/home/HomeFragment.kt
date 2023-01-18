package com.tekinumut.cuyemekhane.home

import android.os.Bundle
import android.util.Log
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
import com.tekinumut.cuyemekhane.databinding.FragmentHomeBinding
import com.tekinumut.cuyemekhane.home.events.HomePageEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by Umut Tekin on 16.01.2023.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    private val homeDailyFoodsAdapter = HomeDailyFoodsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() {
        with(binding) {
            binding.swipeRefreshLayoutRoot.setOnRefreshListener(refreshListener)
            recyclerFoods.adapter = homeDailyFoodsAdapter
            includeErrorLayout.buttonOpenWebsite.setOnClickListener {
                Utility.openWebSiteWithCustomTabs(it.context, Constants.NETWORK.MAIN_PAGE)
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.homePageEvent.collect { mainPageEvent ->
                    when (mainPageEvent) {
                        HomePageEvent.Default -> Unit
                        HomePageEvent.Loading -> {
                            with(binding) {
                                includeErrorLayout.root.hide()
                                progressLoading.isGone = swipeRefreshLayoutRoot.isRefreshing
                            }
                        }
                        is HomePageEvent.Failure -> {
                            binding.progressLoading.hide()
                            binding.includeErrorLayout.root.show()
                            stopRefreshListener()
                            Log.e("BaseApp", "Failure: ${mainPageEvent.exception.errorType}")
                        }
                        is HomePageEvent.Success -> {
                            binding.includeErrorLayout.root.hide()
                            binding.progressLoading.hide()
                            stopRefreshListener()
                            homeDailyFoodsAdapter.submitList(mainPageEvent.dailyMenuUIModel.foodList)
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
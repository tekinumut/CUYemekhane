package com.tekinumut.cuyemekhane.feature.announcements

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseFragment
import com.tekinumut.cuyemekhane.common.extensions.collectWithLifecycle
import com.tekinumut.cuyemekhane.common.extensions.orEmptyString
import com.tekinumut.cuyemekhane.common.extensions.setupToolbar
import com.tekinumut.cuyemekhane.common.helpers.CuAnimationHelper.rotateClockWise
import com.tekinumut.cuyemekhane.common.ui.CuToolbar
import com.tekinumut.cuyemekhane.databinding.FragmentAnnouncementsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnouncementsFragment : BaseFragment<FragmentAnnouncementsBinding>(
    FragmentAnnouncementsBinding::inflate
) {

    private val viewModel by viewModels<AnnouncementsViewModel>()

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
                        viewModel.getAnnouncements()
                    },
                    mainTitle = findNavController().currentDestination?.label.orEmptyString()
                )
            )
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getAnnouncements()
            }
        }
    }

    private fun initObservers() {
        collectWithLifecycle(viewModel.uiState) { uiState ->
            when (uiState.state) {
                AnnouncementsViewModel.State.Initial -> Unit
                is AnnouncementsViewModel.State.AnnouncementsFetched -> {

                }
                AnnouncementsViewModel.State.NoAnnouncements -> {

                }
            }
        }
        collectWithLifecycle(viewModel.event) { event ->
            when (event) {
                AnnouncementsViewModel.Event.ShowLoading -> {
                    with(binding) {
                        progressLoading.isGone = swipeRefreshLayout.isRefreshing
                    }
                }
                AnnouncementsViewModel.Event.HideLoading -> {
                    with(binding) {
                        toolbar.getActionButton.clearAnimation()
                        progressLoading.hide()
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }
}
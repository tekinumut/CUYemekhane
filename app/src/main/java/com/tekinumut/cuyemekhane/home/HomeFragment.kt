package com.tekinumut.cuyemekhane.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tekinumut.cuyemekhane.common.extensions.BaseFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.homePageEvent.collect { mainPageEvent ->
                    when (mainPageEvent) {
                        HomePageEvent.Default -> Unit
                        HomePageEvent.Loading -> {
                            Log.e("BaseApp", "Loading:")
                        }
                        is HomePageEvent.Failure -> {
                            Log.e("BaseApp", "Failure: ${mainPageEvent.exception.errorType}")
                        }
                        is HomePageEvent.Success -> {
                            mainPageEvent.mainPageUIModel.dailyMenu.foodList.forEach {
                                Log.e("BaseApp", "$it \n")
                            }

                        }
                    }
                }
            }
        }
    }
}
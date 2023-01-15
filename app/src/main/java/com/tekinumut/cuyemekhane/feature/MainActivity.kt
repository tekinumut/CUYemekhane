package com.tekinumut.cuyemekhane.feature

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tekinumut.cuyemekhane.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getMainPage()

        lifecycleScope.launch {
            viewModel.mainPageEvent.collect { mainPageEvent ->
                when (mainPageEvent) {
                    MainPageEvent.Default -> Unit
                    MainPageEvent.Loading -> {
                        Log.e("BaseApp", "Loading:")
                    }
                    is MainPageEvent.Failure -> {
                        Log.e("BaseApp", "Failure: ${mainPageEvent.exception.errorType}")
                    }
                    is MainPageEvent.Success -> {
                        Log.e("BaseApp", "Success: ${mainPageEvent.mainPageUIModel.todayDate}")
                    }
                }
            }
        }
    }
}
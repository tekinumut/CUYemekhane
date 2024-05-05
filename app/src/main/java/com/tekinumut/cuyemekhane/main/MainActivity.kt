package com.tekinumut.cuyemekhane.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.extensions.collectWithLifecycle
import com.tekinumut.cuyemekhane.common.extensions.hide
import com.tekinumut.cuyemekhane.common.extensions.show
import com.tekinumut.cuyemekhane.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        init()
        initObservers()
    }

    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        initBottomNavigation()
    }

    private fun initObservers() {
        collectWithLifecycle(viewModel.userPreferences) { preferences ->
            if (preferences.hideBannerAds) {
                binding.adView.hide()
            } else {
                val adRequest = AdRequest.Builder().build()
                binding.adView.loadAd(adRequest)
                binding.adView.show()
            }
        }
    }

    private fun initBottomNavigation() {
        with(binding) {
            bottomNavigationBar.setupWithNavController(navController)
            bottomNavigationBar.setOnItemReselectedListener {
                navController.popBackStack(bottomNavigationBar.selectedItemId, false)
            }
        }
    }
}
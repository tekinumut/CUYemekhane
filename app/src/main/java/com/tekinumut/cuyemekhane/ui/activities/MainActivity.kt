package com.tekinumut.cuyemekhane.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.databinding.ActivityMainBinding
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main)
        binding.mainVM = mainViewModel
        binding.lifecycleOwner = this

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        // Tema değişimi gibi durumlarda çalışma
        if (savedInstanceState == null) {
            navController.setNavStartDestionation()
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(navList(), drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        // Update Action Bar Title
        mainViewModel.actionBarTitle.observe(this, Observer { supportActionBar?.title = it })

        // Seçili item hakkında viewModel'ı bilgilendir
        navController.addOnDestinationChangedListener { _, destination, _ ->
            //nav_view.checkeditem başlangıçta nedense null dönüyor :)
            if (nav_view.checkedItem == null) nav_view.setCheckedItem(destination.id)
            mainViewModel.updateCurrentDestination(destination.id)
        }

        initAdBanner()
    }

    /**
     * AdViewBanner'ın görünüm durumu
     */
    private fun initAdBanner() {
        val mainPref = MainPref.getInstance(this)
        val isBannerSwitchOpen = mainPref.getBoolean(getString(R.string.bannerAdKey),true)
        if(isBannerSwitchOpen){
            // Init ads
            MobileAds.initialize(this) {}
            val adRequest = AdRequest.Builder().build()
            adViewBanner.loadAd(adRequest)
        }
    }

    /**
     * Uygulamanın başlangıçta açılacağı sayfayı belirler
     */
    private fun NavController.setNavStartDestionation() {
        val mainPref: MainPref = MainPref.getInstance(this@MainActivity)
        val defPage: Int = mainPref.getString(getString(R.string.defPageListKey), "0")?.toIntOrNull() ?: 0
        val navGraph = this.navInflater.inflate(R.navigation.nav_graph)

        navGraph.startDestination = when (defPage) {
            0 -> R.id.nav_daily_list
            1 -> R.id.nav_monthly_list
            2 -> R.id.nav_duyurular
            else -> R.id.nav_daily_list
        }

        this.graph = navGraph
    }

    /**
     * Navigation tarafından yüklenecek layout listesi
     */
    private fun navList(): Set<Int> {
        return setOf(R.id.nav_daily_list,
            R.id.nav_monthly_list,
            R.id.nav_duyurular,
            R.id.nav_pricing,
            R.id.nav_contact,
            R.id.nav_settings,
            R.id.nav_about
        )
    }

    /**
     * Navigation Drawer item select listener
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Eğer geri tuşuna bastığımda drawer açıksa önce onu kapat
     */
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}

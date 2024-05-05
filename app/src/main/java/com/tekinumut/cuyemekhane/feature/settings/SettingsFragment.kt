package com.tekinumut.cuyemekhane.feature.settings

import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseFragment
import com.tekinumut.cuyemekhane.common.extensions.collectWithLifecycle
import com.tekinumut.cuyemekhane.common.extensions.setupToolbar
import com.tekinumut.cuyemekhane.common.ui.CuToolbar
import com.tekinumut.cuyemekhane.common.ui.CustomAlertDialogs
import com.tekinumut.cuyemekhane.common.util.ThemeMode
import com.tekinumut.cuyemekhane.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObservers()
    }

    private fun initUI() {
        with(binding) {
            setupToolbar(
                toolbar = toolbar,
                config = CuToolbar.ConfigModel(
                    displayNavIcon = true,
                    navIconListener = { findNavController().popBackStack() },
                    mainTitle = getString(R.string.profile_tab_settings)
                )
            )
        }
    }

    private fun initObservers() {
        collectWithLifecycle(viewModel.userPreferences) { preferences ->
            with(binding) {
                switchHideBannerAds.isChecked = preferences.hideBannerAds
                switchHideBannerAds.setOnCheckedChangeListener(switchChangeListeners)
                linearChangeTheme.setOnClickListener(
                    onChangeThemeChanged(preferences.selectedTheme)
                )
            }
        }
    }

    private val switchChangeListeners = CompoundButton.OnCheckedChangeListener { view, isChecked ->
        with(binding) {
            when (view.id) {
                switchHideBannerAds.id -> viewModel.updateHideBannerAds(isChecked)
            }
        }
    }

    private fun onChangeThemeChanged(currentOption: ThemeMode) = View.OnClickListener { view ->
        val uiModeManager = view.context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        CustomAlertDialogs.showChangeThemeDialog(
            context = view.context,
            currentOption = currentOption,
            onOptionChanged = {
                uiModeManager.setApplicationNightMode(it.mode)
                viewModel.updateChangeTheme(it)
            }
        )
    }
}
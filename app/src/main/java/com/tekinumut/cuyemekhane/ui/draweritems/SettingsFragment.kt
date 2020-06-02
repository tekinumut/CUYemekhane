package com.tekinumut.cuyemekhane.ui.draweritems

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.library.SafePreferenceClickListener
import com.tekinumut.cuyemekhane.library.Utility

class SettingsFragment : PreferenceFragmentCompat() {

    private val nightModeList by lazy { findPreference<ListPreference>(getString(R.string.nightModeListKey))!! }
    private val autoUpdatePref by lazy { findPreference<Preference>(getString(R.string.autoUpdatePrefKey))!! }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        nightModeList.setOnPreferenceChangeListener { _, newValue ->
            Utility.setTheme(newValue.toString().toInt())
            true
        }

        autoUpdatePref.setOnSafePreferenceClickListener {
            val navConroller = Navigation.findNavController(requireView())
            navConroller.navigate(R.id.action_nav_settings_to_autoUpdateDialog)
        }

    }

    private fun Preference.setOnSafePreferenceClickListener(onSafeClick: (Preference) -> Unit) {
        val safeClickListener = SafePreferenceClickListener { onSafeClick(it) }
        onPreferenceClickListener = safeClickListener
    }

}
package com.tekinumut.cuyemekhane.ui.draweritems

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.SafePreferenceClickListener
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private val nightModeList by lazy { findPreference<ListPreference>(getString(R.string.nightModeListKey))!! }
    private val bannerAdSwitch by lazy { findPreference<SwitchPreference>(getString(R.string.bannerAdKey))!! }
    private val autoUpdatePref by lazy { findPreference<Preference>(getString(R.string.autoUpdatePrefKey))!! }
    private val mainVM: MainViewModel by activityViewModels()

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

        bannerAdSwitch.summaryProvider = Preference.SummaryProvider<SwitchPreference> {
            if (it.isChecked) getString(R.string.adSummaryActive)
            else {
                val expireTimeStamp = preferenceManager.sharedPreferences
                    .getLong(getString(R.string.isBannerExpire), ConstantsGeneral.defRewardExpireDate.time)
                val dateOnly = ConstantsGeneral.timeStampToDateString(expireTimeStamp).run { substring(0, length - 9) }
                getString(R.string.adSummaryPassive, dateOnly)
            }
        }

        bannerAdSwitch.setOnPreferenceChangeListener { _, newValue ->
            val status: Boolean = newValue as Boolean
            //Eğer reklamları kapatmak istiyorsa
            if (!status) {
                // Eğer reklamları kapatabilecek şart sağlanmıyorsa.
                if (Utility.isBannerTimeExpired(requireContext())) {
                    onRewardDialogOpen()
                } else { // Sağlanıyorsa reklamları kapat.
                    bannerAdSwitch.isChecked = false
                }
            }
            // Reklamları şart olmadan açabilir :)
            else {
                bannerAdSwitch.isChecked = true
            }

            false
        }

    }

    private fun onRewardDialogOpen() {
        mainVM.isRemoveBannerRewardEarned.observe(requireActivity(), Observer {
            Log.e("ittt", ":::: $it")
            if (it) bannerAdSwitch.isChecked = false
        })
        val navConroller = Navigation.findNavController(requireView())
        navConroller.navigate(R.id.action_nav_settings_to_removeBannerAdDialog)
    }

    private fun Preference.setOnSafePreferenceClickListener(onSafeClick: (Preference) -> Unit) {
        val safeClickListener = SafePreferenceClickListener { onSafeClick(it) }
        onPreferenceClickListener = safeClickListener
    }


}
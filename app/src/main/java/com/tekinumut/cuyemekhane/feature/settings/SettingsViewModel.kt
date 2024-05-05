package com.tekinumut.cuyemekhane.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.common.data.cache.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val userPreferences = dataStoreRepository.userPreferencesFlow

    fun updateHideBannerAds(hideBannerAds: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.updateHideBannerAds(hideBannerAds)
        }
    }
}
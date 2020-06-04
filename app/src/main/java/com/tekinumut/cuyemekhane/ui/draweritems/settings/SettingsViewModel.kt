package com.tekinumut.cuyemekhane.ui.draweritems.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _isRemoveBannerRewardEarned = MutableLiveData(false)

    val isRemoveBannerRewardEarned: LiveData<Boolean> = _isRemoveBannerRewardEarned

    /**
     * Banner reklamı kaldıran diyalog penceresini izler
     * Eğer reklam başarıyla izlendiyse true döner.
     */
    fun updateIsRemoveBannerRewardEearned(isEarned: Boolean) {
        _isRemoveBannerRewardEarned.value = isEarned
    }

}
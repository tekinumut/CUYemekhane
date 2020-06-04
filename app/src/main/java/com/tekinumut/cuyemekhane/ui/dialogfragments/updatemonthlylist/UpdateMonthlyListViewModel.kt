package com.tekinumut.cuyemekhane.ui.dialogfragments.updatemonthlylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpdateMonthlyListViewModel : ViewModel() {

    // tvContent mevcut metni
    private val _remainingTimeText = MutableLiveData<String>()

    // Şuan geri sayım çalışıyor mu ?
    private val _isTimeRunning = MutableLiveData(false)

    // Reklam yükleme durumu
    private val _adStatus = MutableLiveData(0)

    val remainingTimeText: LiveData<String> = _remainingTimeText
    val isTimeRunning: LiveData<Boolean> = _isTimeRunning
    val adStatus: LiveData<Int> = _adStatus

    /**
     * Reklamın yükleme durumunu takip eder
     * 0 -> Yükleniyor
     * 1 -> Başarılı
     * 2 -> Başarısız
     */
    fun updateAdStatus(status: Int) {
        _adStatus.value = status
    }

    /**
     * Geri sayım çalışma durumu
     */
    fun updateTimeRunningStatus(isRunning: Boolean) {
        _isTimeRunning.value = isRunning
        if (isRunning) _adStatus.value = 1
    }

    /**
     * Kalan süre metnini günceller
     */
    fun updateRemainingTimeText(text: String) {
        _remainingTimeText.value = text
    }
}
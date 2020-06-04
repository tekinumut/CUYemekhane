package com.tekinumut.cuyemekhane.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RewardAdDialogViewModel : ViewModel() {

    /**
     * Reklamın yükleme durumunu takip eder
     * 0 -> Yükleniyor
     * 1 -> Başarılı
     * 2 -> Başarısız
     */
    private val _adStatus = MutableLiveData(0)

    val adStatus: LiveData<Int> = _adStatus

    fun updateAdStatus(status: Int) {
        _adStatus.value = status
    }

}
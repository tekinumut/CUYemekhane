package com.tekinumut.cuyemekhane.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthlyListViewModel : ViewModel() {

    // Gösterilecek ekran layoutunu belirler
    private val _isInfo = MutableLiveData(false)
    private val _isListEmpty = MutableLiveData(false)
    private val _adStatus = MutableLiveData(0)

    val adStatus: LiveData<Int> = _adStatus
    val isInfo: LiveData<Boolean> = _isInfo
    val isListEmpty: LiveData<Boolean> = _isListEmpty

    /**
     * Aylık liste verisinin boş olma durumuna göre layout düzenler
     * liste boş işe bilgilendirme layoutu açılır, dolu ise menülerin bulunduğu layout
     */
    fun updateIsInfo(isInfo: Boolean) {
        _isInfo.value = isInfo
    }

    /**
     * Seçili güne ait menünün boş olma duruma
     */
    fun updateIsListEmpty(isListEmpty: Boolean) {
        _isListEmpty.value = isListEmpty
    }

    /**
     * Reklamın yükleme durumunu takip eder
     * 0 -> Yükleniyor
     * 1 -> Başarılı
     * 2 -> Başarısız
     */
    fun updateAdStatus(status: Int) {
        _adStatus.value = status
    }

}
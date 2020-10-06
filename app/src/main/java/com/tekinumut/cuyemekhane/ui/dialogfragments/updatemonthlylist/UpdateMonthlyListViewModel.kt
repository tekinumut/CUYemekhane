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

    // Reklamın sunucudan hatalı dönme sayısı
    // Belirtilen sayıdan fazla hata alınırsa ilgili bölüme reklamsız erişime izin ver.
    // adderrorcode = 3 durumunda count artar.
    private val _adErrorCount = MutableLiveData(0)

    val remainingTimeText: LiveData<String> = _remainingTimeText
    val isTimeRunning: LiveData<Boolean> = _isTimeRunning
    val adStatus: LiveData<Int> = _adStatus
    val adErrorCount: LiveData<Int> = _adErrorCount

    /**
     * Reklamın yükleme durumunu takip eder
     * 0 -> Yükleniyor
     * 1 -> Başarılı
     * 2 -> Başarısız
     * 3 -> Başarısız - İzin ver
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

    /**
     * Sunucudan reklam dönmeyince count değerini arttırır
     */
    fun incAdErrorCount() {
        _adErrorCount.value = _adErrorCount.value?.plus(1) ?: 0
    }

}
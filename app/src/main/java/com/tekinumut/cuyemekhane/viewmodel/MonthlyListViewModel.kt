package com.tekinumut.cuyemekhane.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthlyListViewModel : ViewModel() {

    // Gösterilecek ekran layoutunu belirler
    private val _isInfo = MutableLiveData(false)
    private val _isListEmpty = MutableLiveData(false)

    val isInfo: LiveData<Boolean> = _isInfo
    val isListEmpty: LiveData<Boolean> = _isListEmpty

    /**
     * Aylık liste verisinin boş olma durumuna göre layout düzenler
     * liste boş işe bilgilendirme layoutu açılır, dolu ise menülerin bulunduğu layout
     */
    fun updateIsInfo(isInfo: Boolean) {
        _isInfo.value = isInfo
    }

    fun updateIsListEmpty(isListEmpty: Boolean) {
        _isListEmpty.value = isListEmpty
    }
}
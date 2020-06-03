package com.tekinumut.cuyemekhane.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {

    private val _isLess = MutableLiveData(true)

    val isLess: LiveData<Boolean> = _isLess

    /**
     * Son değeri tersle
     */
    fun updateExpandVal() {
        _isLess.value = !(_isLess.value ?: false)
    }

}
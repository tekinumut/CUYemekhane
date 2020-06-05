package com.tekinumut.cuyemekhane

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // ROOM veritabanı üzerinde işlem yapmamızı sağlayan SQL sorgularına erişir
    private val _actionBarTitle = MutableLiveData<String>()
    private val _currentDestinationID = MutableLiveData<Int>()
    val actionBarTitle: LiveData<String> = _actionBarTitle

    /**
     * ActionBar metnini günceller
     */
    fun updateActionTitle(text: String) {
        _actionBarTitle.value = text
    }

    /**
     * DrawerLayout'ta seçili güncel item id'sini günceller
     */
    fun updateCurrentDestination(destinationID: Int) {
        _currentDestinationID.value = destinationID
    }

    /**
     * Banner reklamının görünüm durumu
     */
    val adBannerVisibility: LiveData<Boolean> = Transformations.map(_currentDestinationID) {
        val mainPref = MainPref.getInstance(application)
        val isBannerSwitchOpen = mainPref.getBoolean(application.getString(R.string.bannerAdKey), true)
        if (isBannerSwitchOpen) {
            it?.let { ConstantsGeneral.adActiveNavList().contains(it) } ?: true
        } else {
            false
        }

    }

}
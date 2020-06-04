package com.tekinumut.cuyemekhane.ui.draweritems.dailylist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tekinumut.cuyemekhane.library.ConstantsOfWebSite
import com.tekinumut.cuyemekhane.library.DataUtility
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.models.DateWithFoodDetailComp
import com.tekinumut.cuyemekhane.models.ListOfAll
import com.tekinumut.cuyemekhane.room.DailyDAO
import com.tekinumut.cuyemekhane.room.DailyDatabase
import kotlinx.coroutines.Dispatchers
import org.jsoup.Jsoup

class DailyListViewModel(application: Application) : AndroidViewModel(application) {

    private val dailyFoodDao: DailyDAO = DailyDatabase.getInstance(application).yemekDao()

    /**
     * Web sitesinden günlük listeleri çeker ve veritabanına kayıt eder
     * @return listenin dizi sayısını dönderir
     *
     */
    fun getDailyListData(imgQuality: Int): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        try {
            // Web sitesinden html verilerini alıp doc'a yaz.
            val doc = Jsoup.connect(ConstantsOfWebSite.MainPageURL).timeout(10000).get()
            val listOfAll: ListOfAll = DataUtility.getDailyList(doc, imgQuality)
            // Alınan verileri veritabanına yaz
            dailyFoodDao.addAllValues(listOfAll)
            // Şu aşamada kullanılmıyor.
            emit(Resource.Success(listOfAll.foodDate.size))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }


    /**
     * Günlük liste değişimlerini takip eder.
     */
    val getDailyList: LiveData<DateWithFoodDetailComp> = dailyFoodDao.getDailyList()
}
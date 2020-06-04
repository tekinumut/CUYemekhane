package com.tekinumut.cuyemekhane.ui.draweritems.duyurular

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.ConstantsOfWebSite
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.models.Duyurular
import com.tekinumut.cuyemekhane.room.DailyDAO
import com.tekinumut.cuyemekhane.room.DailyDatabase
import kotlinx.coroutines.Dispatchers
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class DuyurularViewModel(application: Application) : AndroidViewModel(application) {

    private val dailyFoodDao: DailyDAO = DailyDatabase.getInstance(application).yemekDao()

    /**
     * Duyurular verisini çeker
     */
    fun getDuyurularData(): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        try {
            val duyurulist = ArrayList<Duyurular>()
            val doc = Jsoup.connect(ConstantsOfWebSite.MainPageURL).timeout(10000).get()
            val duyuruClass = doc.select(ConstantsOfWebSite.duyuruClass)
            val title: Elements = doc.select(ConstantsOfWebSite.duyuruTitle)
            val content: Elements = doc.select(ConstantsOfWebSite.duyuruContent)

            duyuruClass.forEachIndexed { i, element ->
                val foodImgURL: String? = element.select("[src]").attr("abs:src")
                val imgBase64 = if (foodImgURL.isNullOrEmpty()) null
                else Utility.imgURLToBase64(foodImgURL, ConstantsGeneral.defDailyImgQuality)
                duyurulist.add(Duyurular(i + 1, title[i].text(), content[i].text(), imgBase64))
            }
            dailyFoodDao.insertDuyurular(duyurulist)
            emit(Resource.Success(0))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    /**
     * Duyurular tablosunu izler
     */
    val getDuyurular: LiveData<List<Duyurular>> = dailyFoodDao.getDuyurular()
}
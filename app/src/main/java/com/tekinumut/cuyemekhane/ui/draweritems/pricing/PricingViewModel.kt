package com.tekinumut.cuyemekhane.ui.draweritems.pricing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tekinumut.cuyemekhane.library.ConstantsOfWebSite
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.models.Pricing
import com.tekinumut.cuyemekhane.room.DailyDAO
import com.tekinumut.cuyemekhane.room.DailyDatabase
import kotlinx.coroutines.Dispatchers
import org.jsoup.Jsoup

class PricingViewModel(application: Application) : AndroidViewModel(application) {

    private val dailyFoodDao: DailyDAO = DailyDatabase.getInstance(application).yemekDao()

    /**
     * Ücretlendirme verilerini indirir
     */
    fun getPricingData(): LiveData<Resource<String>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        try {
            val doc = Jsoup.connect(ConstantsOfWebSite.PricingURL).timeout(10000).get()
            val pricingSectionPart = doc.select("section#section4").outerHtml()

            dailyFoodDao.addPricing(Pricing(1, pricingSectionPart))
            emit(Resource.Success(pricingSectionPart))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    /**
     * Ücretlendirme tablosunu izler
     */
    val getPricing: LiveData<String> = dailyFoodDao.getPricingHtml()

}
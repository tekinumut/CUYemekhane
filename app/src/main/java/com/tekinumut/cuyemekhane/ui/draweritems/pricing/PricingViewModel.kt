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
import com.tekinumut.cuyemekhane.viewmodel.Repository
import kotlinx.coroutines.Dispatchers
import org.jsoup.Jsoup

class PricingViewModel(application: Application) : AndroidViewModel(application) {

    private val dailyFoodDao: DailyDAO = DailyDatabase.getInstance(application).yemekDao()
    private val repository: Repository = Repository(application)

    /**
     * Ücretlendirme verilerini indirir
     */
    fun getPricingData(): LiveData<Resource<String>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        when (val apiCall = repository.getPricingData()) {
            is Resource.Success -> {
                // Ücretlendirme verisini veritabanına yaz
                // Hep aynı id üzerine yaz
                dailyFoodDao.addPricing(Pricing(1, apiCall.data))
                emit(Resource.Success(apiCall.data))
            }
            is Resource.Error -> emit(Resource.Error(apiCall.exception))
        }
    }

    /**
     * Ücretlendirme tablosunu izler
     */
    val getPricing: LiveData<String> = dailyFoodDao.getPricingHtml()

}
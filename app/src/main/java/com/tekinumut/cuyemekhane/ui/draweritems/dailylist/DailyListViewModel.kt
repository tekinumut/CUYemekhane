package com.tekinumut.cuyemekhane.ui.draweritems.dailylist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.models.DateWithFoodDetailComp
import com.tekinumut.cuyemekhane.room.DailyDAO
import com.tekinumut.cuyemekhane.room.DailyDatabase
import com.tekinumut.cuyemekhane.viewmodel.Repository
import kotlinx.coroutines.Dispatchers

class DailyListViewModel(application: Application) : AndroidViewModel(application) {

    private val dailyFoodDao: DailyDAO = DailyDatabase.getInstance(application).yemekDao()
    private val repository: Repository = Repository(application)

    /**
     * Web sitesinden günlük listeleri çeker ve veritabanına kayıt eder
     * @return listenin dizi sayısını dönderir
     *
     */
    fun getDailyListData(imgQuality: Int): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        when (val apiCall = repository.getDailyListData(imgQuality)) {
            is Resource.Success -> {
                // Listeyi veritabanına yaz
                dailyFoodDao.addAllValues(apiCall.data)
                // Şimdilik dönüş değeri kullanılmıyor
                emit(Resource.Success(apiCall.data.foodDate.size))
            }
            is Resource.Error -> emit(Resource.Error(apiCall.exception))
        }
    }

    /**
     * Günlük liste değişimlerini takip eder.
     */
    val getDailyList: LiveData<DateWithFoodDetailComp> = dailyFoodDao.getDailyList()
}
package com.tekinumut.cuyemekhane.ui.draweritems.duyurular

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.models.Duyurular
import com.tekinumut.cuyemekhane.room.DailyDAO
import com.tekinumut.cuyemekhane.room.DailyDatabase
import com.tekinumut.cuyemekhane.viewmodel.Repository
import kotlinx.coroutines.Dispatchers

class DuyurularViewModel(application: Application) : AndroidViewModel(application) {

    private val dailyFoodDao: DailyDAO = DailyDatabase.getInstance(application).yemekDao()
    private val repository: Repository = Repository(application)

    /**
     * Duyurular verisini çeker
     */
    fun getDuyurularData(): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        when (val apiCall = repository.getDuyurularData()) {
            is Resource.Success -> {
                // Duyurlar verisini veritabanına yaz
                dailyFoodDao.insertDuyurular(apiCall.data)
                // Kullanılmayan değer
                emit(Resource.Success(0))
            }
            is Resource.Error -> emit(Resource.Error(apiCall.exception))
        }
    }

    /**
     * Duyurular tablosunu izler
     */
    val getDuyurular: LiveData<List<Duyurular>> = dailyFoodDao.getDuyurular()
}
package com.tekinumut.cuyemekhane.ui.draweritems.monthlylist

import android.app.Application
import androidx.lifecycle.*
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.models.DateWithFoodDetailComp
import com.tekinumut.cuyemekhane.models.FoodDate
import com.tekinumut.cuyemekhane.room.MonthlyDAO
import com.tekinumut.cuyemekhane.room.MonthlyDatabase
import com.tekinumut.cuyemekhane.viewmodel.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MonthlyListViewModel(application: Application) : AndroidViewModel(application) {

    private val monthlyFoodDao: MonthlyDAO = MonthlyDatabase.getInstance(application).yemekDao()
    private val repository: Repository = Repository(application)

    // Gösterilecek ekran layoutunu belirler
    private val _isInfo = MutableLiveData(false)

    // Seçili güne ait menü boşsa uygun layoutu getir
    private val _isListEmpty = MutableLiveData(false)

    val isInfo: LiveData<Boolean> = _isInfo
    val isListEmpty: LiveData<Boolean> = _isListEmpty

    /**
     * Web sitesinden aylık listeleri çeker ve veritabanına kayıt eder
     * @return listenin dizi sayısını dönderir
     *
     */
    fun getMonthlyFoodData(imgQuality: Int): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        when (val apiCall = repository.getMonthlyFoodData(imgQuality)) {
            is Resource.Success -> {
                // Listeyi veritabanına yaz
                monthlyFoodDao.addAllValues(apiCall.data)
                // Şimdilik dönüş değeri kullanılmıyor
                emit(Resource.Success(apiCall.data.foodDate.size))
            }
            is Resource.Error -> emit(Resource.Error(apiCall.exception))
        }
    }

    /**
     * Aylık liste değişimlerini takip eder
     */
    val getDaysOfMonth: LiveData<List<FoodDate>> = monthlyFoodDao.getDaysOfMonth()

    /**
     * Aylık listeden seçilmiş olan güne ait verileri getirir.
     */
    fun getSelectedDayOfMonth(): LiveData<DateWithFoodDetailComp> = monthlyFoodDao.getSelectedDayOfMonth()

    /**
     * Aylık listeden seçilen tarihe ait yemek listesini siler
     */
    fun removeSelectedDay() = viewModelScope.launch(Dispatchers.IO) {
        monthlyFoodDao.removeSelectedDayOfMonth()
    }

    /**
     * Aylık liste ekranında gösterilen menüyü günceller
     */
    fun updateSelectedDay(datePart: String) = viewModelScope.launch(Dispatchers.IO) {
        monthlyFoodDao.updateSelectedDayOfMonth(datePart)
    }

    /**
     * Aylık liste verisinin boş olma durumuna göre layout düzenler
     * liste boş işe bilgilendirme layoutu açılır, dolu ise menülerin bulunduğu layout
     */
    fun updateIsInfo(isInfo: Boolean) {
        _isInfo.value = isInfo
    }

    /**
     * Seçili güne ait menünün boş olma duruma
     */
    fun updateIsListEmpty(isListEmpty: Boolean) {
        _isListEmpty.value = isListEmpty
    }

}
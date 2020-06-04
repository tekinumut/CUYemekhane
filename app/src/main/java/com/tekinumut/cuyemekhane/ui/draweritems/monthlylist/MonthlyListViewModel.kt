package com.tekinumut.cuyemekhane.ui.draweritems.monthlylist

import android.app.Application
import androidx.lifecycle.*
import com.tekinumut.cuyemekhane.library.ConstantsOfWebSite
import com.tekinumut.cuyemekhane.library.DataUtility
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.models.DateWithFoodDetailComp
import com.tekinumut.cuyemekhane.models.FoodDate
import com.tekinumut.cuyemekhane.models.ListOfAll
import com.tekinumut.cuyemekhane.models.specificmodels.MonthlyDialogCallBackModel
import com.tekinumut.cuyemekhane.room.MonthlyDAO
import com.tekinumut.cuyemekhane.room.MonthlyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class MonthlyListViewModel(application: Application) : AndroidViewModel(application) {

    private val monthlyFoodDao: MonthlyDAO = MonthlyDatabase.getInstance(application).yemekDao()

    // Gösterilecek ekran layoutunu belirler
    private val _isInfo = MutableLiveData(false)

    // Seçili güne ait menü boşsa uygun layoutu getir
    private val _isListEmpty = MutableLiveData(false)

    // Reklam başarıyla izlendiğinde isSuccess true olarak döner
    private val _isMonthlyListRewardEarned = MutableLiveData<MonthlyDialogCallBackModel>()

    val isMonthlyListRewardEarned: LiveData<MonthlyDialogCallBackModel> = _isMonthlyListRewardEarned
    val isInfo: LiveData<Boolean> = _isInfo
    val isListEmpty: LiveData<Boolean> = _isListEmpty

    /**
     * Web sitesinden aylık listeleri çeker ve veritabanına kayıt eder
     * @return listenin dizi sayısını dönderir
     *
     */
    fun getMonthlyFoodData(imgQuality: Int): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        try {
            // Web sitesinden html verilerini alıp doc'a yaz.
            val doc = Jsoup.connect(ConstantsOfWebSite.MainPageURL).timeout(30000).get()
            val listOfAll: ListOfAll = DataUtility.getMonthlyList(doc, imgQuality)
            // Alınan verileri veritabanına yaz
            monthlyFoodDao.addAllValues(listOfAll)
            // Şu aşamada kullanılmıyor.
            emit(Resource.Success(listOfAll.foodDate.size))
        } catch (e: Exception) {
            emit(Resource.Error(e))
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

    /**
     * Aylık listeyi yüklemek için izlenmesi gereken reklamın izlenme durumu
     */
    fun updateIsMonthlyListRewardEearned(model: MonthlyDialogCallBackModel) {
        _isMonthlyListRewardEarned.value = model
    }

}
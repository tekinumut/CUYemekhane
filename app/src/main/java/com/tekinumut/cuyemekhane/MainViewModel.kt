package com.tekinumut.cuyemekhane

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.ConstantsOfWebSite
import com.tekinumut.cuyemekhane.library.DataUtility
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.models.DateWithFoodDetailComp
import com.tekinumut.cuyemekhane.models.ListOfAll
import com.tekinumut.cuyemekhane.room.DailyDatabase
import com.tekinumut.cuyemekhane.room.FoodDAO
import com.tekinumut.cuyemekhane.room.MonthlyDatabase
import kotlinx.coroutines.Dispatchers
import org.jsoup.Jsoup

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // ROOM veritabanı üzerinde işlem yapmamızı sağlayan SQL sorgularına erişir
    private val monthlyFoodDao: FoodDAO = MonthlyDatabase.getInstance(application).yemekDao()
    private val dailyFoodDao: FoodDAO = DailyDatabase.getInstance(application).yemekDao()
    private val _actionBarTitle = MutableLiveData<String>()
    val actionBarTitle: LiveData<String> = _actionBarTitle

    /**
     * @param type ConstantsGeneral.dbNameDaily || ConstantsGeneral.dbNameMonthly değerlerinden birini alır
     * Alınan değere göre ilgili veritabanına verileri yazar
     * @return aylık veya günlük listeyi dönderir.
     *
     */
    fun getFoodData(type: String): LiveData<Resource<ListOfAll>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        try {
            val selectedDAO = if (type == ConstantsGeneral.dbNameDaily) dailyFoodDao else monthlyFoodDao
            // Web sitesinden html verilerini alıp doc'a yaz.
            val doc = Jsoup.connect(ConstantsOfWebSite.URL).timeout(20000).get()
            // Type'a göre günlük veya aylık liste verilerini init et
            val listOfAll: ListOfAll = if (type == ConstantsGeneral.dbNameDaily) {
                DataUtility.getDailyList(doc)
            } else {
                DataUtility.getMonthlyList(doc)
            }
            // Alınan verileri veritabanına yaz
            listOfAll.run { selectedDAO.addAllValues(foodDate, food, foodDetail, foodComponent) }
            // Alınan veriyi döndür.
            emit(Resource.Success(listOfAll))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }

    }

    /**
     * Aylık liste değişimlerini takip eder
     */
    val getMonthlyList: LiveData<List<DateWithFoodDetailComp>> = monthlyFoodDao.getMonthlyList()

    /**
     * Günlük liste değişimlerini takip eder.
     */
    val getDailyList: LiveData<DateWithFoodDetailComp> = dailyFoodDao.getDailyList()

    /**
     * ActionBar metnini günceller
     */
    fun updateActionTitle(text: String) {
        _actionBarTitle.value = text
    }

}
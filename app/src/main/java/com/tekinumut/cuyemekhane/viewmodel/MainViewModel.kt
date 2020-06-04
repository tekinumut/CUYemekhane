package com.tekinumut.cuyemekhane.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.library.*
import com.tekinumut.cuyemekhane.models.*
import com.tekinumut.cuyemekhane.models.specificmodels.MonthlyDialogCallBackModel
import com.tekinumut.cuyemekhane.room.DailyDAO
import com.tekinumut.cuyemekhane.room.DailyDatabase
import com.tekinumut.cuyemekhane.room.MonthlyDAO
import com.tekinumut.cuyemekhane.room.MonthlyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // ROOM veritabanı üzerinde işlem yapmamızı sağlayan SQL sorgularına erişir
    private val monthlyFoodDao: MonthlyDAO = MonthlyDatabase.getInstance(application).yemekDao()
    private val dailyFoodDao: DailyDAO = DailyDatabase.getInstance(application).yemekDao()
    private val _actionBarTitle = MutableLiveData<String>()
    private val _currentDestinationID = MutableLiveData<Int>()
    private val _isRemoveBannerRewardEarned = MutableLiveData(false)
    private val _isMonthlyListRewardEarned = MutableLiveData<MonthlyDialogCallBackModel>()
    val actionBarTitle: LiveData<String> = _actionBarTitle
    val isRemoveBannerRewardEarned: LiveData<Boolean> = _isRemoveBannerRewardEarned
    val isMonthlyListRewardEarned: LiveData<MonthlyDialogCallBackModel> = _isMonthlyListRewardEarned

    /**
     * @param type ConstantsGeneral.dbNameDaily || ConstantsGeneral.dbNameMonthly değerlerinden birini alır
     * Alınan değere göre ilgili veritabanına verileri yazar
     * @return listenin dizi sayısını dönderir
     *
     */
    fun getFoodData(type: String, imgQuality: Int): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        emit(Resource.InProgress)
        try {
            // Web sitesinden html verilerini alıp doc'a yaz.
            val doc = Jsoup.connect(ConstantsOfWebSite.MainPageURL)
                .timeout(if (type == ConstantsGeneral.dbNameDaily) 10000 else 30000).get()
            // Type'a göre günlük veya aylık liste verilerini init et
            val listOfAll: ListOfAll = if (type == ConstantsGeneral.dbNameDaily) {
                DataUtility.getDailyList(doc, imgQuality)
            } else {
                DataUtility.getMonthlyList(doc, imgQuality)
            }
            // Alınan verileri veritabanına yaz
            when (type) {
                ConstantsGeneral.dbNameDaily -> dailyFoodDao.addAllValues(listOfAll)
                ConstantsGeneral.dbNameMonthly -> monthlyFoodDao.addAllValues(listOfAll)
            }
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

    fun updateSelectedDay(datePart: String) = viewModelScope.launch(Dispatchers.IO) {
        monthlyFoodDao.updateSelectedDayOfMonth(datePart)
    }

    /**
     * Günlük liste değişimlerini takip eder.
     */
    val getDailyList: LiveData<DateWithFoodDetailComp> = dailyFoodDao.getDailyList()

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

    /**
     * Banner reklamı kaldıran diyalog penceresini izler
     * Eğer reklam başarıyla izlendiyse true döner.
     */
    fun updateIsRemoveBannerRewardEearned(isEarned: Boolean) {
        _isRemoveBannerRewardEarned.value = isEarned
    }

    /**
     * Aylık listeyi yüklemek için izlenmesi gereken reklamın izlenme durumu
     */
    fun updateIsMonthlyListRewardEearned(model: MonthlyDialogCallBackModel) {
        _isMonthlyListRewardEarned.value = model
    }


}
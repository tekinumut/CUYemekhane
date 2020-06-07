package com.tekinumut.cuyemekhane.viewmodel

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.tekinumut.cuyemekhane.library.*
import com.tekinumut.cuyemekhane.models.Duyurular
import com.tekinumut.cuyemekhane.models.ListOfAll
import org.jsoup.Jsoup
import org.jsoup.select.Elements

@Suppress("BlockingMethodInNonBlockingContext")

class Repository(context: Context) {

    private val firebaseAnalytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    /**
     * Error Handiling
     */
    private suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T, requestName: String): Resource<T> {
        return try {
            Resource.Success(apiCall.invoke())
        } catch (e: Exception) {
            val bundle = Bundle()
            bundle.putString(requestName, "$e")
            firebaseAnalytics.logEvent(ConstantsGeneral.firebaseConnError, bundle)
            Resource.Error(e)
        }
    }

    /**
     * Günlük liste verilerini al
     */
    suspend fun getDailyListData(imgQuality: Int): Resource<ListOfAll> = safeApiCall({
        val doc = Jsoup.connect(ConstantsOfWebSite.MainPageURL).timeout(10000).get()
        DataUtility.getDailyList(doc, imgQuality)
    }, ConstantsGeneral.connErrorDaily)

    /**
     * Aylık liste verilerini al
     */
    suspend fun getMonthlyFoodData(imgQuality: Int): Resource<ListOfAll> = safeApiCall({
        val doc = Jsoup.connect(ConstantsOfWebSite.MainPageURL).timeout(30000).get()
        DataUtility.getMonthlyList(doc, imgQuality)
    }, ConstantsGeneral.connErrorMonthly)

    /**
     * Ücretlendirme verilerini al
     */
    suspend fun getPricingData(): Resource<String> = safeApiCall({
        val doc = Jsoup.connect(ConstantsOfWebSite.PricingURL).timeout(10000).get()
        doc.select("section#section4").outerHtml()
    }, ConstantsGeneral.connErrorPricing)

    /**
     * Duyuruların verisini al
     */
    suspend fun getDuyurularData(): Resource<ArrayList<Duyurular>> = safeApiCall({
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
        duyurulist
    }, ConstantsGeneral.connErrorDuyurular)

}
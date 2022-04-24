package com.tekinumut.cuyemekhane.viewmodel

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.tekinumut.cuyemekhane.library.*
import com.tekinumut.cuyemekhane.models.Duyurular
import com.tekinumut.cuyemekhane.models.ListOfAll
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class Repository(context: Context) {

    private val firebaseAnalytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    /**
     * Error Handiling
     */
    private suspend fun <T : Any> safeApiCall(
        apiCall: suspend () -> T,
        requestName: String
    ): Resource<T> {
        return try {
            Resource.Success(apiCall.invoke())
        } catch (e: Exception) {
            val bundle = Bundle()
            bundle.putString(requestName, "$e")
            firebaseAnalytics.logEvent(ConstantsGeneral.firebaseConnError, bundle)
            Resource.Error(e)
        }
    }

    private fun String.baseDocument(timeOut: Int = 10000): Document =
        Jsoup.connect(this)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .timeout(timeOut)
            .get()

    /**
     * Günlük liste verilerini al
     */
    suspend fun getDailyListData(isDlImage: Boolean): Resource<ListOfAll> = safeApiCall({
        val doc = ConstantsOfWebSite.MainPageURL.baseDocument()
        DataUtility.getDailyList(doc, isDlImage)
    }, ConstantsGeneral.connErrorDaily)

    /**
     * Aylık liste verilerini al
     */
    suspend fun getMonthlyFoodData(isDlImage: Boolean): Resource<ListOfAll> = safeApiCall({
        val doc = ConstantsOfWebSite.MainPageURL.baseDocument(30000)
        DataUtility.getMonthlyList(doc, isDlImage)
    }, ConstantsGeneral.connErrorMonthly)

    /**
     * Ücretlendirme verilerini al
     */
    suspend fun getPricingData(): Resource<String> = safeApiCall({
        val doc = ConstantsOfWebSite.PricingURL.baseDocument()
        doc.select("section#section4").outerHtml()
    }, ConstantsGeneral.connErrorPricing)

    /**
     * Duyuruların verisini al
     */
    suspend fun getDuyurularData(): Resource<ArrayList<Duyurular>> = safeApiCall({
        val duyurulist = ArrayList<Duyurular>()
        val doc = ConstantsOfWebSite.MainPageURL.baseDocument()
        val duyuruClass = doc.select(ConstantsOfWebSite.duyuruClass)
        val title: Elements = doc.select(ConstantsOfWebSite.duyuruTitle)
        val content: Elements = doc.select(ConstantsOfWebSite.duyuruContent)

        duyuruClass.forEachIndexed { i, element ->
            val foodImgURL: String? = element.select("[src]").attr("abs:src")
            val imgBase64 = if (foodImgURL.isNullOrEmpty()) null
            else Utility.imgURLToBase64(foodImgURL)
            duyurulist.add(Duyurular(i + 1, title[i].text(), content[i].text(), imgBase64))
        }
        duyurulist
    }, ConstantsGeneral.connErrorDuyurular)

}
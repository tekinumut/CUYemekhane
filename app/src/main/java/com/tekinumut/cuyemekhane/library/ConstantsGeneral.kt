package com.tekinumut.cuyemekhane.library

import java.text.SimpleDateFormat
import java.util.*

class ConstantsGeneral {
    companion object {

        // Aylık yemek listesini tutan room Database
        const val dbNameMonthly = "monthly_db"

        // Günlük yemek listesini tutan room Database
        const val dbNameDaily = "daily_db"

        // Günlük listenin her açılışta en fazla 1 kez çalışmasını sağlar
        const val prefCheckDailyListUpdated = "dailyListUpdated"

        // Navigation'un hangi listeden çalışacağı bilgisini alır.
        const val dailyFragmentKey = 0
        const val monthlyFragmentKey = 1

        // Component Adapter bundle key değerleri
        const val bundleFoodName = "foodName"
        const val bundleImgKey = "imgBase64"
        const val bundleComponentListKey = "componentList"

        // Varsayılan zaman formatı
        val defaultSDF: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

        // Aylık listenin varsayılan resim kalitesi, yüzde cinsinden
        const val defMonthlyImgQuality: Int = 50

        // Aylık listenin varsayılan resim kalitesi, yüzde cinsinden
        const val defDailyImgQuality: Int = 100

    }
}
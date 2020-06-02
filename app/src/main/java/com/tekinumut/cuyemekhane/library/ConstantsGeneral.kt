package com.tekinumut.cuyemekhane.library

import java.text.SimpleDateFormat
import java.util.*

class ConstantsGeneral {
    companion object {

        // Aylık yemek listesini tutan room Database
        const val dbNameMonthly = "monthly_db"

        // Günlük yemek listesini tutan room Database
        const val dbNameDaily = "daily_db"

        // Metodun uygulama açıkken daha önce çalıştığını kontrol eden pref keyleri
        const val prefCheckDailyListWorkedBefore = "dailyListWorkedBefore"
        const val prefCheckDuyurularWorkedBefore = "duyurularWorkedBefore"
        const val prefCheckPricingWorkedBefore = "pricingWorkedBefore"

        // Ayarlar'da bulunan otomatik güncelleme pref keyleri
        const val prefDailyAutoUpdateKey = "dailyAutoUpdateKey"
        const val prefDuyurularAutoUpdateKey = "duyurularAutoUpdateKey"
        const val prefPricingAutoUpdateKey = "pricingAutoUpdateKey"

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

        // Günlük listenin sharedPref'teki otomatik güncelleme varsayılanı
        const val defValDailyAutoUpdate = true
        const val defValDuyurularAutoUpdate = true
        const val defValPricingAutoUpdate = false

    }
}
package com.tekinumut.cuyemekhane.library

import com.tekinumut.cuyemekhane.R
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

        // CountDownTimer kalan süre pref key
        const val prefMonthlyCountDown = "prefMonthlyCountDown"

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


        /**
         * Reklamların gösterileceği layout listesi
         */
        fun adActiveNavList(): Set<Int> {
            return setOf(R.id.nav_daily_list,
                R.id.nav_monthly_list,
                R.id.nav_duyurular,
                R.id.nav_pricing,
                R.id.nav_contact,
                R.id.updateMonthlyListDialog
            )
        }

        private val expireTimeSdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)

        // Varsayılan reklam bitim zamanı
        val defRewardExpireDate: Date = expireTimeSdf.parse("01-01-1970 00:00:00")!!

        // Şuanki zamanı getirir
        fun currentTimeStamp(): Long = expireTimeSdf.parse(expireTimeSdf.format(Date()))!!.time

        // TimeStamp'i uygun formatta zaman metnine çevirir
        fun timeStampToDateString(timeStamp: Long): String = expireTimeSdf.format(Date(timeStamp))

        // Varsayılan aylık listeyi sınırsız yenileme süresi saniye cinsinden
        const val defUpdateMonthlyListDelayTime: Long = 60 * 60

        // Varsayılan RemoveBanner reklam silme süresi
        const val defRemoveBannerDelaytime: Long = 60 * 60 * 24 * 14

        // Firebase Log Event Keys
        const val firebaseConnError: String = "conn_errors"

        const val connErrorDaily = "${firebaseConnError}_daily"
        const val connErrorMonthly = "${firebaseConnError}_monthly"
        const val connErrorPricing = "${firebaseConnError}_pricing"
        const val connErrorDuyurular = "${firebaseConnError}_duyurular"

    }

}
package com.tekinumut.cuyemekhane

import android.app.Application
import android.content.Context
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Utility

@Suppress("unused")
class MainApplication : Application() {
    private lateinit var mainPref: MainPref
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mainPref = MainPref.getInstance(baseContext)

        clearIsAppOpenedBeforePref()
        setNightModeTheme()
        checkIsBannerExpire(base)
    }

    /**
     * Banner reklam gizleme süresi dolmuş mu diye bak
     */
    private fun checkIsBannerExpire(context: Context?) {
        context?.let {
            if (Utility.isBannerTimeExpired(context)) {
                mainPref.save(context.getString(R.string.bannerAdKey), true)
            }
        }
    }

    /**
     * Uygulamanın ilk kez açıldığını talep eden preflere bildiriyoruz
     */
    private fun clearIsAppOpenedBeforePref() {
        mainPref.save(ConstantsGeneral.prefCheckDailyListWorkedBefore, false)
        mainPref.save(ConstantsGeneral.prefCheckDuyurularWorkedBefore, false)
        mainPref.save(ConstantsGeneral.prefPricingAutoUpdateKey, false)
    }

    /**
     * Uygulamanın karanlık teması ayarlanıyor
     * Kullanıcı daha önce bir seçim yaptıysa o seçim başta uygulanır
     * yapmadıysa '-1' değeri ile varsayılan cihaz teması uygulanır
     */
    private fun setNightModeTheme() {
        val typeOfNightMode = mainPref.getString(getString(R.string.nightModeListKey), "-1")!!.toInt()
        Utility.setTheme(typeOfNightMode)
    }
}
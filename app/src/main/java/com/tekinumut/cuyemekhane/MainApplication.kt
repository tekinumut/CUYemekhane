package com.tekinumut.cuyemekhane

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Utility

@Suppress("unused")
class MainApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
        val mainPref = MainPref.getInstance(baseContext)

        clearIsAppOpenedBeforePref(mainPref)

        setNightModeTheme(mainPref)
    }

    /**
     * Uygulamanın ilk kez açıldığını talep eden preflere bildiriyoruz
     */
    private fun clearIsAppOpenedBeforePref(mainPref: MainPref) {
        mainPref.save(ConstantsGeneral.prefCheckDailyListWorkedBefore, false)
        mainPref.save(ConstantsGeneral.prefCheckDuyurularWorkedBefore, false)
        mainPref.save(ConstantsGeneral.prefPricingAutoUpdateKey, false)
    }

    /**
     * Uygulamanın karanlık teması ayarlanıyor
     * Kullanıcı daha önce bir seçim yaptıysa o seçim başta uygulanır
     * yapmadıysa '-1' değeri ile varsayılan cihaz teması uygulanır
     */
    private fun setNightModeTheme(mainPref: MainPref) {
        val typeOfNightMode = mainPref.getString(getString(R.string.nightModeListKey), "-1")!!.toInt()
        Utility.setTheme(typeOfNightMode)
    }
}
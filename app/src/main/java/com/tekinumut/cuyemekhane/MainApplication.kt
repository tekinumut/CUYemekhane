package com.tekinumut.cuyemekhane

import android.app.Application
import android.content.Context
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref

class MainApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        val mainPref = MainPref.getInstance(baseContext)
        mainPref.save(ConstantsGeneral.prefCheckDailyListUpdated, false)
    }
}
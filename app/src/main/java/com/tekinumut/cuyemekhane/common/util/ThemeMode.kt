package com.tekinumut.cuyemekhane.common.util

import android.app.UiModeManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate.NightMode
import com.tekinumut.cuyemekhane.R

enum class ThemeMode(@NightMode val mode: Int, @StringRes val textResource: Int) {
    Default(UiModeManager.MODE_NIGHT_AUTO, R.string.settings_change_theme_text_default),
    Light(UiModeManager.MODE_NIGHT_NO, R.string.settings_change_theme_text_light),
    Dark(UiModeManager.MODE_NIGHT_YES, R.string.settings_change_theme_text_dark);

    companion object {
        fun getEntry(mode: Int?): ThemeMode = ThemeMode.entries.find {
            it.mode == mode
        } ?: Default
    }
}
package com.tekinumut.cuyemekhane.common.data.cache

import com.tekinumut.cuyemekhane.common.util.ThemeMode

data class UserPreferences(
    val hideBannerAds: Boolean,
    val selectedTheme: ThemeMode
)
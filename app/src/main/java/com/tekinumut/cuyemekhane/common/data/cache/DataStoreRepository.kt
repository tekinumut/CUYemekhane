package com.tekinumut.cuyemekhane.common.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.tekinumut.cuyemekhane.common.util.ThemeMode
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        val hideBannerAds = preferences[PreferencesKeys.HIDE_BANNER_ADS] ?: false
        val selectedTheme = preferences[PreferencesKeys.SELECTED_THEME]
        UserPreferences(
            hideBannerAds = hideBannerAds,
            selectedTheme = ThemeMode.getEntry(selectedTheme)
        )
    }

    suspend fun updateHideBannerAds(hideBannerAds: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_BANNER_ADS] = hideBannerAds
        }
    }

    suspend fun updateSelectedTheme(selectedTheme: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_THEME] = selectedTheme.mode
        }
    }

    private object PreferencesKeys {
        val HIDE_BANNER_ADS = booleanPreferencesKey("hide_banner_ads")
        val SELECTED_THEME = intPreferencesKey("selected_theme")
    }
}
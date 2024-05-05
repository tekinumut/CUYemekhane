package com.tekinumut.cuyemekhane.common.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        val hideBannerAds = preferences[PreferencesKeys.HIDE_BANNER_ADS] ?: false
        UserPreferences(
            hideBannerAds = hideBannerAds
        )
    }

    suspend fun updateHideBannerAds(hideBannerAds: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_BANNER_ADS] = hideBannerAds
        }
    }

    private object PreferencesKeys {
        val HIDE_BANNER_ADS = booleanPreferencesKey("hide_banner_ads")
    }
}
package com.tekinumut.cuyemekhane.main

import androidx.lifecycle.ViewModel
import com.tekinumut.cuyemekhane.common.data.cache.DataStoreRepository
import com.tekinumut.cuyemekhane.common.data.cache.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MainViewModel @Inject constructor(
    dataRepository: DataStoreRepository
) : ViewModel() {

    val userPreferences: Flow<UserPreferences> = dataRepository.userPreferencesFlow
}
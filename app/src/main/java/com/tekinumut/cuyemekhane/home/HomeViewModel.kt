package com.tekinumut.cuyemekhane.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.domain.usecase.TodayMenuUseCase
import com.tekinumut.cuyemekhane.home.events.HomePageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Umut Tekin on 16.01.2023.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val todayMenuUseCase: TodayMenuUseCase
) : ViewModel() {

    init {
        fetchMainPage()
    }

    private val _homePageEvent = MutableStateFlow<HomePageEvent>(HomePageEvent.Default)
    val homePageEvent: StateFlow<HomePageEvent> = _homePageEvent

    fun fetchMainPage() {
        viewModelScope.launch {
            todayMenuUseCase(Unit).collect { resource ->
                when (resource) {
                    Resource.Loading -> _homePageEvent.value = HomePageEvent.Loading
                    is Resource.Failure -> {
                        _homePageEvent.value = HomePageEvent.Failure(resource.cuError)
                    }
                    is Resource.Success -> {
                        _homePageEvent.value = HomePageEvent.Success(resource.value)
                    }
                }
            }
        }
    }
}
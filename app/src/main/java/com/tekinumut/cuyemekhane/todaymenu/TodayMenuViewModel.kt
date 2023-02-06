package com.tekinumut.cuyemekhane.todaymenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.domain.usecase.TodayMenuUseCase
import com.tekinumut.cuyemekhane.todaymenu.events.TodayMenuEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Umut Tekin on 16.01.2023.
 */
@HiltViewModel
class TodayMenuViewModel @Inject constructor(
    private val todayMenuUseCase: TodayMenuUseCase
) : ViewModel() {

    init {
        fetchMainPage()
    }

    private val _todayMenuEvent = MutableStateFlow<TodayMenuEvent>(TodayMenuEvent.Default)
    val todayMenuEvent: StateFlow<TodayMenuEvent> = _todayMenuEvent

    fun fetchMainPage() {
        viewModelScope.launch {
            todayMenuUseCase(Unit).collect { resource ->
                when (resource) {
                    Resource.Loading -> _todayMenuEvent.value = TodayMenuEvent.Loading
                    is Resource.Failure -> {
                        _todayMenuEvent.value = TodayMenuEvent.Failure(resource.cuError)
                    }
                    is Resource.Success -> {
                        _todayMenuEvent.value = TodayMenuEvent.Success(resource.value)
                    }
                }
            }
        }
    }
}
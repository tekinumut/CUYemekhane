package com.tekinumut.cuyemekhane.todaymenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayMenuUIModel
import com.tekinumut.cuyemekhane.common.domain.usecase.TodayMenuUseCase
import com.tekinumut.cuyemekhane.todaymenu.events.TodayMenuEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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

    private val _todayMenuEvent = MutableStateFlow(UIState())
    val todayMenuEvent: StateFlow<UIState> = _todayMenuEvent

    private val _event = Channel<Event>(capacity = Channel.UNLIMITED)
    val event: Flow<Event> = _event.receiveAsFlow()

    fun fetchMainPage() {
        showLoading()
        viewModelScope.launch {
            todayMenuUseCase(Unit).collect { resource ->
                when (resource) {
                    is TodayMenuEvent.Success -> {
                        _todayMenuEvent.update {
                            it.copy(
                                state = State.MenuFetched,
                                menu = it.menu
                            )
                        }
                        hideLoading()
                    }
                    TodayMenuEvent.EmptyList,
                    is TodayMenuEvent.Failure -> {
                        _todayMenuEvent.update {
                            it.copy(
                                state = State.NoMenuFound
                            )
                        }
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        viewModelScope.launch {
            _event.send(Event.HideLoading)
        }
    }

    private fun showLoading() {
        viewModelScope.launch {
            _event.send(Event.ShowLoading)
        }
    }

    sealed interface Event {
        object ShowLoading : Event
        object HideLoading : Event
    }

    data class UIState(
        val state: State = State.Initial,
        val menu: TodayMenuUIModel? = null
    )

    enum class State {
        Initial,
        MenuFetched,
        NoMenuFound
    }
}
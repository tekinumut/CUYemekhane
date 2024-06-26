package com.tekinumut.cuyemekhane.feature.todaymenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.common.domain.model.todayfood.TodayMenuUIModel
import com.tekinumut.cuyemekhane.common.domain.usecase.TodayMenuUseCase
import com.tekinumut.cuyemekhane.common.domain.usecase.events.TodayMenuEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TodayMenuViewModel @Inject constructor(
    private val todayMenuUseCase: TodayMenuUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    private val _event = Channel<Event>(capacity = Channel.UNLIMITED)
    val event: Flow<Event> = _event.receiveAsFlow()

    init {
        fetchDailyMenu()
    }

    fun fetchDailyMenu() {
        showLoading()
        viewModelScope.launch {
            todayMenuUseCase(Unit).collect { resource ->
                when (resource) {
                    is TodayMenuEvent.Success -> {
                        _uiState.update {
                            it.copy(state = State.MenuFetched(resource.todayMenuUIModel))
                        }
                        hideLoading()
                    }
                    TodayMenuEvent.EmptyList,
                    is TodayMenuEvent.Failure -> {
                        _uiState.update { it.copy(state = State.NoMenuFound) }
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
        data object ShowLoading : Event
        data object HideLoading : Event
    }

    data class UIState(
        val state: State = State.Initial
    )

    sealed interface State {
        data object Initial : State
        data class MenuFetched(val menu: TodayMenuUIModel) : State
        data object NoMenuFound : State
    }
}
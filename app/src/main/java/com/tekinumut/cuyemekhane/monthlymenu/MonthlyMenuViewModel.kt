package com.tekinumut.cuyemekhane.monthlymenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.MonthlyMenuUIModel
import com.tekinumut.cuyemekhane.common.domain.usecase.MonthlyMenuUseCase
import com.tekinumut.cuyemekhane.monthlymenu.events.MonthlyMenuEvent
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
class MonthlyMenuViewModel @Inject constructor(
    private val monthlyMenuUseCase: MonthlyMenuUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    private val _event = Channel<Event>(capacity = Channel.UNLIMITED)
    val event: Flow<Event> = _event.receiveAsFlow()

    fun fetchMonthlyMenu() {
        showLoading()
        viewModelScope.launch {
            monthlyMenuUseCase(Unit).collect { resource ->
                when (resource) {
                    is MonthlyMenuEvent.Success -> {
                        _uiState.update {
                            it.copy(
                                state = State.MenuFetched,
                                menu = resource.monthlyMenuUIModel
                            )
                        }
                        hideLoading()
                    }
                    MonthlyMenuEvent.EmptyList,
                    is MonthlyMenuEvent.Failure -> {
                        _uiState.update {
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
        val menu: MonthlyMenuUIModel? = null
    )

    enum class State {
        Initial,
        MenuFetched,
        NoMenuFound
    }
}
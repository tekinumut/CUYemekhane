package com.tekinumut.cuyemekhane.feature.announcements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.common.domain.model.announcements.AnnouncementsUIModel
import com.tekinumut.cuyemekhane.common.domain.usecase.AnnouncementsUseCase
import com.tekinumut.cuyemekhane.common.domain.usecase.events.AnnouncementsEvent
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
class AnnouncementsViewModel @Inject constructor(
    private val announcementsUseCase: AnnouncementsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    private val _event = Channel<Event>(capacity = Channel.UNLIMITED)
    val event: Flow<Event> = _event.receiveAsFlow()

    init {
        getAnnouncements()
    }

    fun getAnnouncements() {
        showLoading()
        viewModelScope.launch {
            announcementsUseCase(Unit).collect { event ->
                when (event) {
                    is AnnouncementsEvent.Success -> {
                        _uiState.update {
                            UIState(State.AnnouncementsFetched(event.announcements))
                        }
                        hideLoading()
                    }
                    is AnnouncementsEvent.NoAnnouncement -> {
                        _uiState.update {
                            UIState(state = State.NoAnnouncement(event.announcements))
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
        data object ShowLoading : Event
        data object HideLoading : Event
    }

    data class UIState(
        val state: State = State.Initial
    )

    sealed interface State {
        data object Initial : State
        data class AnnouncementsFetched(val announcements: List<AnnouncementsUIModel>) : State
        data class NoAnnouncement(val announcements: List<AnnouncementsUIModel>) : State
    }
}
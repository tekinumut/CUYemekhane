package com.tekinumut.cuyemekhane.feature.monthlymenudetail

import androidx.lifecycle.ViewModel
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyFoodUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MonthlyMenuDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    data class UIState(
        val state: State = State.Initial,
        val menu: List<DailyFoodUIModel> = emptyList()
    )

    fun updateDailyMenuList(dailyFoods: List<DailyFoodUIModel>) {
        _uiState.update {
            it.copy(
                state = State.MenuFetched,
                menu = dailyFoods
            )
        }
    }

    enum class State {
        Initial,
        MenuFetched
    }
}
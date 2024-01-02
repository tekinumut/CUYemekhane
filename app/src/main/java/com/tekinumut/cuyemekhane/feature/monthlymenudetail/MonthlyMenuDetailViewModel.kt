package com.tekinumut.cuyemekhane.feature.monthlymenudetail

import androidx.lifecycle.ViewModel
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyFoodUIModel
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyFoodUIModel.Companion.convertToTodayFoodUIModel
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayFoodUIModel
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
        val state: State = State.Initial
    )

    fun updateDailyMenuList(dailyFoods: List<DailyFoodUIModel>) {
        val todayFoodMenuList = dailyFoods.map { it.convertToTodayFoodUIModel() }
        _uiState.update { it.copy(state = State.MenuFetched(todayFoodMenuList)) }
    }

    sealed interface State {
        data object Initial : State
        data class MenuFetched(val todayFoods: List<TodayFoodUIModel>) : State
    }
}
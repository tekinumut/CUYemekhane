package com.tekinumut.cuyemekhane.common.domain.usecase.events

import com.tekinumut.cuyemekhane.common.data.api.response.CuError
import com.tekinumut.cuyemekhane.common.domain.model.todayfood.TodayMenuUIModel

sealed interface TodayMenuEvent {
    data class Success(
        val todayMenuUIModel: TodayMenuUIModel
    ) : TodayMenuEvent

    data class Failure(val exception: CuError) : TodayMenuEvent
    data object EmptyList : TodayMenuEvent
}
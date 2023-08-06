package com.tekinumut.cuyemekhane.feature.todaymenu.events

import com.tekinumut.cuyemekhane.common.data.api.response.CuError
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayMenuUIModel

sealed interface TodayMenuEvent {
    data class Success(
        val todayMenuUIModel: TodayMenuUIModel
    ) : TodayMenuEvent

    class Failure(val exception: CuError) : TodayMenuEvent
    data object EmptyList : TodayMenuEvent
}
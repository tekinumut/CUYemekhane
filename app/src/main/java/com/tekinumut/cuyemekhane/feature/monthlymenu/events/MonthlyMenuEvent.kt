package com.tekinumut.cuyemekhane.feature.monthlymenu.events

import com.tekinumut.cuyemekhane.common.data.api.response.CuError
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.MonthlyMenuUIModel

sealed interface MonthlyMenuEvent {

    data class Success(
        val monthlyMenuUIModel: MonthlyMenuUIModel
    ) : MonthlyMenuEvent

    class Failure(val exception: CuError) : MonthlyMenuEvent
    data object EmptyList : MonthlyMenuEvent
}
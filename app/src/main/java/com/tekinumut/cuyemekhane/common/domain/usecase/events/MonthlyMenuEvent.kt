package com.tekinumut.cuyemekhane.common.domain.usecase.events

import com.tekinumut.cuyemekhane.common.data.api.response.CuError
import com.tekinumut.cuyemekhane.common.domain.model.monthlyfood.MonthlyMenuUIModel

sealed interface MonthlyMenuEvent {

    data class Success(
        val monthlyMenuUIModel: MonthlyMenuUIModel
    ) : MonthlyMenuEvent

    class Failure(val exception: CuError) : MonthlyMenuEvent
    data object EmptyList : MonthlyMenuEvent
}
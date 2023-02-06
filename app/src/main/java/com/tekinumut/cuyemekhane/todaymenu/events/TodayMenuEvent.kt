package com.tekinumut.cuyemekhane.todaymenu.events

import com.tekinumut.cuyemekhane.common.data.model.response.CuError
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayMenuUIModel

/**
 * Created by Umut Tekin on 15.01.2023.
 */
sealed class TodayMenuEvent {
    data class Success(
        val todayMenuUIModel: TodayMenuUIModel
    ) : TodayMenuEvent()

    class Failure(val exception: CuError) : TodayMenuEvent()
    object Loading : TodayMenuEvent()
    object Default : TodayMenuEvent()
}
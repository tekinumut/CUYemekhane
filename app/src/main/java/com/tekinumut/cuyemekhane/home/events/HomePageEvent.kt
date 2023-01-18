package com.tekinumut.cuyemekhane.home.events

import com.tekinumut.cuyemekhane.common.data.model.response.CuError
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyMenuUIModel

/**
 * Created by Umut Tekin on 15.01.2023.
 */
sealed class HomePageEvent {
    data class Success(
        val dailyMenuUIModel: DailyMenuUIModel
    ) : HomePageEvent()

    class Failure(val exception: CuError) : HomePageEvent()
    object Loading : HomePageEvent()
    object Default : HomePageEvent()
}
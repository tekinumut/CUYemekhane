package com.tekinumut.cuyemekhane.feature

import com.tekinumut.cuyemekhane.common.data.model.response.CuError
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.MainPageUIModel

/**
 * Created by Umut Tekin on 15.01.2023.
 */
sealed class MainPageEvent {
    data class Success(
        val mainPageUIModel: MainPageUIModel
    ) : MainPageEvent()

    class Failure(val exception: CuError) : MainPageEvent()
    object Loading : MainPageEvent()
    object Default : MainPageEvent()
}
package com.tekinumut.cuyemekhane.common.data.model.mappers

import com.tekinumut.cuyemekhane.common.data.model.mainpage.MainPageResponse
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.MainPageUIModel
import javax.inject.Inject

/**
 * Created by Umut Tekin on 15.01.2023.
 */
class MainPageMapper @Inject constructor(
    private val dailyMenuMapper: DailyMenuMapper
) : ApiMapper<MainPageResponse, MainPageUIModel> {
    override fun mapToUIModel(responseModel: MainPageResponse?): MainPageUIModel {
        return MainPageUIModel(
            dailyMenu = dailyMenuMapper.mapToUIModel(responseModel?.dailyMenu)
        )
    }
}
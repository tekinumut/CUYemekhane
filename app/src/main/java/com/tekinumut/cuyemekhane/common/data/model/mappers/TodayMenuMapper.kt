package com.tekinumut.cuyemekhane.common.data.model.mappers

import com.tekinumut.cuyemekhane.common.data.model.mainpage.TodayMenu
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayMenuUIModel
import javax.inject.Inject

/**
 * Created by Umut Tekin on 16.01.2023.
 */
class TodayMenuMapper @Inject constructor(
    private val todayFoodMapper: TodayFoodMapper
) : ApiMapper<TodayMenu, TodayMenuUIModel> {
    override fun mapToUIModel(responseModel: TodayMenu?): TodayMenuUIModel {
        return TodayMenuUIModel(
            date = responseModel?.date.orEmpty(),
            foods = responseModel?.foods?.map {
                todayFoodMapper.mapToUIModel(it)
            }.orEmpty()
        )
    }
}
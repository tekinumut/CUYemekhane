package com.tekinumut.cuyemekhane.common.data.mappers

import com.tekinumut.cuyemekhane.common.data.model.todayfood.TodayMenu
import com.tekinumut.cuyemekhane.common.domain.model.todayfood.TodayMenuUIModel
import javax.inject.Inject

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
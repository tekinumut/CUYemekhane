package com.tekinumut.cuyemekhane.common.data.model.mappers

import com.tekinumut.cuyemekhane.common.data.model.mainpage.DailyMenu
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyMenuUIModel
import javax.inject.Inject

class DailyMenuMapper @Inject constructor(
    private val foodMapper: FoodMapper
) : ApiMapper<DailyMenu, DailyMenuUIModel> {
    override fun mapToUIModel(responseModel: DailyMenu?): DailyMenuUIModel {
        return DailyMenuUIModel(
            date = responseModel?.date.orEmpty(),
            foodList = responseModel?.foodList?.map {
                foodMapper.mapToUIModel(it)
            }.orEmpty()
        )
    }
}
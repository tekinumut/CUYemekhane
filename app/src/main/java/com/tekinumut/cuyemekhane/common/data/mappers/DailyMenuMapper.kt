package com.tekinumut.cuyemekhane.common.data.mappers

import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.DailyMenu
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyMenuUIModel
import com.tekinumut.cuyemekhane.common.extensions.toZeroIfNull
import javax.inject.Inject

class DailyMenuMapper @Inject constructor(
    private val dailyFoodMapper: DailyFoodMapper
) : ApiMapper<DailyMenu, DailyMenuUIModel> {
    override fun mapToUIModel(responseModel: DailyMenu?): DailyMenuUIModel {
        return DailyMenuUIModel(
            date = responseModel?.date.orEmpty(),
            totalCalorie = responseModel?.totalCalorie.toZeroIfNull(),
            foodList = responseModel?.foodList?.map {
                dailyFoodMapper.mapToUIModel(it)
            }.orEmpty()
        )
    }
}
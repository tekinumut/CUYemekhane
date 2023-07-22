package com.tekinumut.cuyemekhane.common.data.mappers

import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.DailyFood
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.toZeroIfNull
import javax.inject.Inject

class DailyFoodMapper @Inject constructor() : ApiMapper<DailyFood, DailyFoodUIModel> {
    override fun mapToUIModel(responseModel: DailyFood?): DailyFoodUIModel {
        return DailyFoodUIModel(
            name = responseModel?.name.orEmpty(),
            imageUrl = responseModel?.imageUrl.orEmpty(),
            calorie = responseModel?.calorie?.toIntOrNull().toZeroIfNull()
        )
    }
}
package com.tekinumut.cuyemekhane.common.data.model.mappers

import com.tekinumut.cuyemekhane.common.data.model.mainpage.DailyFood
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.toZeroIfNull
import javax.inject.Inject

/**
 * Created by Umut Tekin on 16.01.2023.
 */
class DailyFoodMapper @Inject constructor() : ApiMapper<DailyFood, DailyFoodUIModel> {
    override fun mapToUIModel(responseModel: DailyFood?): DailyFoodUIModel {
        return DailyFoodUIModel(
            name = responseModel?.name.orEmpty(),
            imageUrl = responseModel?.imageUrl.orEmpty(),
            calorie = responseModel?.calorie?.toIntOrNull().toZeroIfNull()
        )
    }
}
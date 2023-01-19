package com.tekinumut.cuyemekhane.common.data.model.mappers

import com.tekinumut.cuyemekhane.common.data.model.mainpage.TodayFood
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.toZeroIfNull
import javax.inject.Inject

/**
 * Created by Umut Tekin on 16.01.2023.
 */
class TodayFoodMapper @Inject constructor() : ApiMapper<TodayFood, TodayFoodUIModel> {
    override fun mapToUIModel(responseModel: TodayFood?): TodayFoodUIModel {
        return TodayFoodUIModel(
            name = responseModel?.name.orEmpty(),
            imageUrl = responseModel?.imageUrl.orEmpty(),
            category = responseModel?.category.orEmpty(),
            calorie = responseModel?.calorie?.toIntOrNull().toZeroIfNull()
        )
    }
}
package com.tekinumut.cuyemekhane.common.data.model.mappers

import com.tekinumut.cuyemekhane.common.data.model.mainpage.Food
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.FoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.toZeroIfNull
import javax.inject.Inject

/**
 * Created by Umut Tekin on 16.01.2023.
 */
class FoodMapper @Inject constructor() : ApiMapper<Food, FoodUIModel> {
    override fun mapToUIModel(responseModel: Food?): FoodUIModel {
        return FoodUIModel(
            name = responseModel?.name.orEmpty(),
            imageUrl = responseModel?.imageUrl.orEmpty(),
            category = responseModel?.category.orEmpty(),
            calorie = responseModel?.calorie?.toIntOrNull().toZeroIfNull()
        )
    }
}
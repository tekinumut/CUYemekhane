package com.tekinumut.cuyemekhane.common.domain.model.mainpage

import com.tekinumut.cuyemekhane.common.data.mappers.UIModel

data class DailyFoodUIModel(
    val name: String,
    val calorie: Int,
    val imageUrl:String
) : UIModel
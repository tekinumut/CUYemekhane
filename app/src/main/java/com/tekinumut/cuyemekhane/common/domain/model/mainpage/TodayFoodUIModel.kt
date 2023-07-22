package com.tekinumut.cuyemekhane.common.domain.model.mainpage

import com.tekinumut.cuyemekhane.common.data.mappers.UIModel

data class TodayFoodUIModel(
    val name: String,
    val category: String,
    val calorie: Int,
    val imageUrl:String
) : UIModel
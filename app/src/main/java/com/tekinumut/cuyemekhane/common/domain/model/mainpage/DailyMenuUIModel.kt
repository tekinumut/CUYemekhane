package com.tekinumut.cuyemekhane.common.domain.model.mainpage

import com.tekinumut.cuyemekhane.common.data.mappers.UIModel

data class DailyMenuUIModel(
    val date: String,
    val totalCalorie: Int,
    val foodList: List<DailyFoodUIModel>
) : UIModel
package com.tekinumut.cuyemekhane.common.domain.model.mainpage

import com.tekinumut.cuyemekhane.common.data.model.mappers.UIModel

data class DailyMenuUIModel(
    val date: String,
    val foodList: List<FoodUIModel>
):UIModel

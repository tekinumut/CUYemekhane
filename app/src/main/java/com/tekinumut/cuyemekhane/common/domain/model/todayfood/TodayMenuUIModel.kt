package com.tekinumut.cuyemekhane.common.domain.model.todayfood

import com.tekinumut.cuyemekhane.common.data.mappers.UIModel

data class TodayMenuUIModel(
    val date: String,
    val foods: List<TodayFoodUIModel>
) : UIModel
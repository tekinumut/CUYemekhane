package com.tekinumut.cuyemekhane.common.data.model.monthlyfood

import com.tekinumut.cuyemekhane.common.data.mappers.ResponseModel

data class DailyMenu(
    val date: String?,
    val totalCalorie: Int?,
    val foodList: List<DailyFood>?
) : ResponseModel

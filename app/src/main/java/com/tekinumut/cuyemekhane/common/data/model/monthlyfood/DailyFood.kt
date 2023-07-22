package com.tekinumut.cuyemekhane.common.data.model.monthlyfood

import com.tekinumut.cuyemekhane.common.data.mappers.ResponseModel

data class DailyFood(
    val name: String?,
    val calorie: String?,
    val imageUrl: String?
) : ResponseModel
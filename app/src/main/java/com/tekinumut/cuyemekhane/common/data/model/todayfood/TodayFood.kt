package com.tekinumut.cuyemekhane.common.data.model.todayfood

import com.tekinumut.cuyemekhane.common.data.mappers.ResponseModel

data class TodayFood(
    val name: String?,
    val category: String?,
    val calorie: String?,
    val imageUrl: String?
) : ResponseModel
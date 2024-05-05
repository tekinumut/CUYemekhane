package com.tekinumut.cuyemekhane.common.data.model.todayfood

import com.tekinumut.cuyemekhane.common.data.mappers.ResponseModel

data class TodayMenu(
    val date: String?,
    val foods: List<TodayFood>?
) : ResponseModel
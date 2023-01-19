package com.tekinumut.cuyemekhane.common.data.model.mainpage

import com.tekinumut.cuyemekhane.common.data.model.mappers.ResponseModel

data class MonthlyMenu(
    val dailyMenuList: List<DailyMenu>?
) : ResponseModel

data class DailyMenu(
    val date: String?,
    val foodList: List<Food>?
) : ResponseModel
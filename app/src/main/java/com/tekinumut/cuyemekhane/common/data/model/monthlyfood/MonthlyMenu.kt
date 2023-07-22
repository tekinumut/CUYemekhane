package com.tekinumut.cuyemekhane.common.data.model.monthlyfood

import com.tekinumut.cuyemekhane.common.data.mappers.ResponseModel

data class MonthlyMenu(
    val dailyMenuList: List<DailyMenu>?
) : ResponseModel
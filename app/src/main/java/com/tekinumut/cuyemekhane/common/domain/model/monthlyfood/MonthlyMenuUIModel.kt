package com.tekinumut.cuyemekhane.common.domain.model.monthlyfood

import com.tekinumut.cuyemekhane.common.data.mappers.UIModel

data class MonthlyMenuUIModel(
    val dailyMenuList: List<DailyMenuUIModel>
) : UIModel
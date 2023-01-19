package com.tekinumut.cuyemekhane.common.domain.model.mainpage

import com.tekinumut.cuyemekhane.common.data.model.mappers.UIModel

data class MonthlyMenuUIModel(
    val dailyMenuList: List<DailyMenuUIModel>
) : UIModel
package com.tekinumut.cuyemekhane.common.data.model.mappers

import com.tekinumut.cuyemekhane.common.data.model.mainpage.MonthlyMenu
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.MonthlyMenuUIModel
import javax.inject.Inject

class MonthlyMenuMapper @Inject constructor(
    private val dailyMenuMapper: DailyMenuMapper
) : ApiMapper<MonthlyMenu, MonthlyMenuUIModel> {
    override fun mapToUIModel(responseModel: MonthlyMenu?): MonthlyMenuUIModel {
        return MonthlyMenuUIModel(
            dailyMenuList = responseModel?.dailyMenuList?.map {
                dailyMenuMapper.mapToUIModel(it)
            }.orEmpty()
        )
    }
}
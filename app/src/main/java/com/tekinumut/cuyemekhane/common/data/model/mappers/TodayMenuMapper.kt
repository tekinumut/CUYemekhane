package com.tekinumut.cuyemekhane.common.data.model.mappers

import com.tekinumut.cuyemekhane.common.data.model.mainpage.TodayMenu
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayMenuUIModel
import javax.inject.Inject

/**
 * Created by Umut Tekin on 16.01.2023.
 */
class TodayMenuMapper @Inject constructor(
    private val foodMapper: FoodMapper
) : ApiMapper<TodayMenu, TodayMenuUIModel> {
    override fun mapToUIModel(responseModel: TodayMenu?): TodayMenuUIModel {
        return TodayMenuUIModel(
            date = responseModel?.date.orEmpty(),
            foodList = responseModel?.foodList?.map {
                foodMapper.mapToUIModel(it)
            }.orEmpty()
        )
    }
}
package com.tekinumut.cuyemekhane.common.domain.model.mainpage

import com.tekinumut.cuyemekhane.common.data.model.mappers.UIModel

/**
 * Created by Umut Tekin on 16.01.2023.
 */
data class DailyFoodUIModel(
    val name: String,
    val calorie: Int,
    val imageUrl:String
) : UIModel
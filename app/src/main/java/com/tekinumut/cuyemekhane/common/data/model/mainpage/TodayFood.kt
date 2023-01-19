package com.tekinumut.cuyemekhane.common.data.model.mainpage

import com.tekinumut.cuyemekhane.common.data.model.mappers.ResponseModel

/**
 * Created by Umut Tekin on 16.01.2023.
 */
data class TodayFood(
    val name: String?,
    val category: String?,
    val calorie: String?,
    val imageUrl: String?
) : ResponseModel
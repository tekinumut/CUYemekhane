package com.tekinumut.cuyemekhane.common.domain.model.mainpage

import android.os.Parcelable
import com.tekinumut.cuyemekhane.common.data.mappers.UIModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyFoodUIModel(
    val name: String,
    val calorie: Int,
    val detailUrl: String,
    val imageUrl: String
) : UIModel, Parcelable
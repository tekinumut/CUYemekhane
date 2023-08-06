package com.tekinumut.cuyemekhane.common.domain.model.mainpage

import android.os.Parcelable
import com.tekinumut.cuyemekhane.common.data.mappers.UIModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyMenuUIModel(
    val date: String,
    val totalCalorie: Int,
    val foodList: List<DailyFoodUIModel>
) : UIModel, Parcelable
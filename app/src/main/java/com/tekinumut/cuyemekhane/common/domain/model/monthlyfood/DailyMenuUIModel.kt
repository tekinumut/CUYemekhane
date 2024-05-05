package com.tekinumut.cuyemekhane.common.domain.model.monthlyfood

import android.os.Parcelable
import androidx.annotation.Keep
import com.tekinumut.cuyemekhane.common.data.mappers.UIModel
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DailyMenuUIModel(
    val date: String,
    val totalCalorie: Int,
    val foodList: List<DailyFoodUIModel>
) : UIModel, Parcelable
package com.tekinumut.cuyemekhane.common.domain.model.monthlyfood

import android.os.Parcelable
import com.tekinumut.cuyemekhane.common.data.mappers.UIModel
import com.tekinumut.cuyemekhane.common.domain.model.todayfood.TodayFoodUIModel
import com.tekinumut.cuyemekhane.common.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyFoodUIModel(
    val name: String,
    val calorie: Int,
    val detailUrl: String,
    val imageUrl: String
) : UIModel, Parcelable {
    companion object {
        fun DailyFoodUIModel.convertToTodayFoodUIModel() = TodayFoodUIModel(
            name = name,
            category = Constants.STRING_EMPTY,
            calorie = calorie,
            imageUrl = imageUrl
        )
    }
}
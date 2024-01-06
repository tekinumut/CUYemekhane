package com.tekinumut.cuyemekhane.common.extensions

import androidx.navigation.NavController
import com.tekinumut.cuyemekhane.common.domain.model.monthlyfood.DailyMenuUIModel
import com.tekinumut.cuyemekhane.common.ui.FullScreenImageDialogDirections
import com.tekinumut.cuyemekhane.feature.monthlymenu.MonthlyMenuFragmentDirections

fun NavController.navigateToMonthlyMenuDetail(dailyMenuUIModel: DailyMenuUIModel) {
    val action = MonthlyMenuFragmentDirections.actionToMonthlyMenuDetailBottomSheet(
        dailyMenuUIModel
    )
    navigate(action)
}

fun NavController.navigateToFullScreenImage(image: String) {
    val action = FullScreenImageDialogDirections.actionGlobalFullScreenImageDialog(image)
    navigate(action)
}
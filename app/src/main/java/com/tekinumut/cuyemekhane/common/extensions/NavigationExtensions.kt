package com.tekinumut.cuyemekhane.common.extensions

import androidx.navigation.NavController
import com.tekinumut.cuyemekhane.common.ui.FullScreenImageDialogDirections

fun NavController.navigateToFullScreenImage(image: String) {
    val action = FullScreenImageDialogDirections.actionGlobalFullScreenImageDialog(image)
    navigate(action)
}
package com.tekinumut.cuyemekhane.models.specificmodels

import com.tekinumut.cuyemekhane.library.ConstantsGeneral

data class MonthlyDialogCallBackModel(
    val isSuccess: Boolean = false,
    val isRefresh: Boolean = false,
    val imgQuality: Int = ConstantsGeneral.defMonthlyImgQuality
)
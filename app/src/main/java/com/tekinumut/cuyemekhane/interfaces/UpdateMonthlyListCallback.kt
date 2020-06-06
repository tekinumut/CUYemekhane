package com.tekinumut.cuyemekhane.interfaces

import com.tekinumut.cuyemekhane.models.specificmodels.MonthlyDialogCallBackModel

interface UpdateMonthlyListCallback {
    fun onAdWatched(callback: MonthlyDialogCallBackModel)
}
package com.tekinumut.cuyemekhane.common.extensions

import com.tekinumut.cuyemekhane.common.util.Constants

fun Int?.toZeroIfNull(): Int = this ?: Constants.INT_ZERO

fun Int?.isBiggerThanZero(func: (Int) -> Unit) {
    this?.let {
        if (it > 0) {
            func(it)
        }
    }
}
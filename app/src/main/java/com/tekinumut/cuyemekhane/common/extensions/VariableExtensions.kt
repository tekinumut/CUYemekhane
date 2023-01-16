package com.tekinumut.cuyemekhane.common.extensions

import com.tekinumut.cuyemekhane.common.util.Constants

/**
 * Created by Umut Tekin on 16.01.2023.
 */

fun Int?.toZeroIfNull(): Int = this ?: Constants.INT_ZERO
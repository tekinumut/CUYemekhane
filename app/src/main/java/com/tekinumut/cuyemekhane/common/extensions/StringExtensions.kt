package com.tekinumut.cuyemekhane.common.extensions

import com.tekinumut.cuyemekhane.common.util.Constants

fun String.withBaseUrl() = "${Constants.NETWORK.BASE_ENDPOINT}$this"

fun String?.toZeroOrNull(): Int = this?.toIntOrNull().toZeroIfNull()
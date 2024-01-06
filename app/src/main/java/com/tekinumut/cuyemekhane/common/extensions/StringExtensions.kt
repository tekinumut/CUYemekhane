package com.tekinumut.cuyemekhane.common.extensions

import com.tekinumut.cuyemekhane.common.util.Constants

fun String.withBaseUrl() = "${Constants.NETWORK.BASE_ENDPOINT}$this"

fun String?.toZeroOrNull(): Int = this?.toIntOrNull().toZeroIfNull()

fun String?.ifEmptyTo(default: String): String = if (isNullOrEmpty()) default else this

fun CharSequence?.orEmptyString(): String = this?.toString().orEmpty()
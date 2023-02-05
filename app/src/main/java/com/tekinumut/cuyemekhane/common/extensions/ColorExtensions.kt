package com.tekinumut.cuyemekhane.common.extensions

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.android.material.color.MaterialColors

@ColorInt
fun Context.resolveColorAttribute(
    @AttrRes attrColor: Int
): Int {
    return MaterialColors.getColor(this, attrColor, Color.BLACK)
}
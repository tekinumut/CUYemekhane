package com.tekinumut.cuyemekhane.common.extensions

import android.view.View
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide

fun ImageView.setImageUrl(url: String?, hideIfNull: Boolean = false) {
    if (!url.isNullOrBlank()) {
        show()
        Glide.with(context).load(url).into(this)
    } else if (hideIfNull) {
        hide()
    }
}

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isGone = true
}
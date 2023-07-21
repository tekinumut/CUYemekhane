package com.tekinumut.cuyemekhane.common.extensions

import android.view.View
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImageUrl(
    url: String?,
    hideIfNull: Boolean = false,
    requestOptions: RequestOptions? = null
) {
    if (!url.isNullOrBlank()) {
        show()
        val glide = Glide.with(context).load(url)
        requestOptions?.let { glide.apply(it) }
        glide.into(this)
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
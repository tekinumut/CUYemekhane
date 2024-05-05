package com.tekinumut.cuyemekhane.common.extensions

import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
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

fun TextView.setTextOrHide(newText: String?) {
    isGone = newText.isNullOrBlank()
    text = newText
}

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isGone = true
}

fun WebView.loadBase64Data(data: String) {
    loadData(data, "text/html", "base64")
}
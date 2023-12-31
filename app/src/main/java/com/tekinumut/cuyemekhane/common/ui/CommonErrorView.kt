package com.tekinumut.cuyemekhane.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.databinding.LayoutErrorBinding

class CommonErrorView(context: Context, attr: AttributeSet) : ConstraintLayout(context, attr) {

    private val binding: LayoutErrorBinding = LayoutErrorBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var positiveClickListener: (v: View) -> Unit = {}

    fun setData(error: CommonErrorModel) {
        with(binding) {
            imageIcon.setImageResource(error.drawableId)
            textDescription.text = context.getString(error.description)
            buttonPositive.text = context.getString(error.positiveButtonText)
            buttonPositive.setOnClickListener { positiveClickListener(it) }
        }
    }

    fun setPositiveListener(listener: (v: View) -> Unit) {
        positiveClickListener = listener
    }
}

data class CommonErrorModel(
    @DrawableRes val drawableId: Int,
    @StringRes val description: Int,
    @StringRes val positiveButtonText: Int
) {
    companion object {
        val menuNotFoundError = CommonErrorModel(
            drawableId = R.drawable.ic_error_info,
            description = R.string.error_message_no_data,
            positiveButtonText = R.string.open_website_text
        )
    }
}
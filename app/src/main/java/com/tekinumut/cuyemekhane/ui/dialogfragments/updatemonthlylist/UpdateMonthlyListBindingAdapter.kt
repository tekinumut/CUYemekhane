package com.tekinumut.cuyemekhane.ui.dialogfragments.updatemonthlylist

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.tekinumut.cuyemekhane.R

@BindingAdapter(value = ["customTextAndColor"])
fun statusOfErrorText(textView: TextView, adStatus: Int) {

    val colorID = if (adStatus == 3) R.color.green else R.color.red
    val textID = if (adStatus == 3)
        R.string.ad_load_fail_error_no_ad
    else
        R.string.ad_load_fail_try_again

    textView.setTextColor(ContextCompat.getColor(textView.context, colorID))
    textView.text = textView.context.getString(textID)

}

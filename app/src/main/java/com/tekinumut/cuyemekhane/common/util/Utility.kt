package com.tekinumut.cuyemekhane.common.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.extensions.resolveColorAttribute

object Utility {

    fun openWebSiteWithCustomTabs(context: Context, url: String) {
        val colorSchemeBuilder = CustomTabColorSchemeParams.Builder().run {
            setToolbarColor(context.resolveColorAttribute(androidx.appcompat.R.attr.colorPrimary))
                .build()
        }
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder().run {
            setDefaultColorSchemeParams(colorSchemeBuilder)
            setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
        }
        val tabIntent = builder.build()
        tabIntent.launchUrl(context, Uri.parse(url))
    }
}
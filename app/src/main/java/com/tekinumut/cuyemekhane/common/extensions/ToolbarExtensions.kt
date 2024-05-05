package com.tekinumut.cuyemekhane.common.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tekinumut.cuyemekhane.common.ui.CuToolbar

fun AppCompatActivity.setupToolbar(
    toolbar: CuToolbar,
    config: CuToolbar.ConfigModel = CuToolbar.ConfigModel()
) {
    setSupportActionBar(toolbar.getBar)
    supportActionBar?.run {
        setDisplayHomeAsUpEnabled(config.displayNavIcon)
        setDisplayShowTitleEnabled(false)
        if (config.navIcon != CuToolbar.NO_ICON) {
            setHomeAsUpIndicator(config.navIcon)
        }
    }
    toolbar.build(config)
}

fun Fragment.setupToolbar(
    toolbar: CuToolbar,
    config: CuToolbar.ConfigModel = CuToolbar.ConfigModel()
) {
    (requireActivity() as? AppCompatActivity)?.setupToolbar(
        toolbar = toolbar,
        config = config
    )
}
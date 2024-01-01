package com.tekinumut.cuyemekhane.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.extensions.hide
import com.tekinumut.cuyemekhane.common.extensions.show
import com.tekinumut.cuyemekhane.common.util.Constants
import com.tekinumut.cuyemekhane.databinding.LayoutToolbarBinding

class CuToolbar(context: Context, attr: AttributeSet) : ConstraintLayout(context, attr) {

    private val binding: LayoutToolbarBinding = LayoutToolbarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    val getBar get() = binding.cuToolbar

    val getActionButton get() = binding.buttonAction

    fun build(config: ConfigModel) {
        with(binding) {
            imageLogo.setImageResource(config.mainLogo)
            setNavIcon(config)
            setActionButton(config)
        }
    }

    private fun setNavIcon(config: ConfigModel) {
        with(binding.cuToolbar) {
            if (config.navIcon != NO_ICON) {
                setNavigationIcon(config.navIcon)
            }
            config.navIconListener?.let { setNavigationOnClickListener(it) }
        }
    }

    private fun setActionButton(config: ConfigModel) {
        with(binding.buttonAction) {
            if (config.actionIcon == NO_ICON) {
                hide()
            } else {
                setImageDrawable(ContextCompat.getDrawable(context, config.actionIcon))
                config.actionButtonListener?.let { setOnClickListener(it) }
                show()
            }
        }
    }

    data class ConfigModel(
        @DrawableRes val navIcon: Int = NO_ICON,
        val displayNavIcon: Boolean = false,
        val navIconListener: OnClickListener? = null,
        @DrawableRes val actionIcon: Int = NO_ICON,
        val actionButtonListener: OnClickListener? = null,
        @DrawableRes val mainLogo: Int = R.drawable.logo
    )

    companion object {
        const val NO_ICON = Constants.INT_ZERO
    }
}
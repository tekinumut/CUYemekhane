package com.tekinumut.cuyemekhane.common.helpers

import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

object CuAnimationHelper {

    fun View.rotateClockWise() {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f

        ).apply {
            interpolator = LinearInterpolator()
            duration = 500L
            repeatCount = Animation.INFINITE
        }
        startAnimation(rotateAnimation)
    }
}
package com.tekinumut.cuyemekhane.common.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

typealias InflateFragment<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

inline fun <T : ViewBinding> ViewGroup.viewBinding(
    viewBindingFactory:
        (LayoutInflater, ViewGroup, Boolean) -> T
) = viewBindingFactory.invoke(LayoutInflater.from(this.context), this, false)
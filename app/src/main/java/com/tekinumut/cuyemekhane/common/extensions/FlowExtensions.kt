package com.tekinumut.cuyemekhane.common.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> AppCompatActivity.collectWithLifecycle(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.CREATED,
    crossinline onError: (Throwable) -> Unit = {},
    crossinline block: (T) -> Unit
) {
    lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        onError(throwable)
    }) {
        repeatOnLifecycle(state) {
            flow.collect {
                block(it)
            }
        }
    }
}

inline fun <T> Fragment.collectWithLifecycle(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.CREATED,
    crossinline onError: (Throwable) -> Unit = {},
    crossinline block: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        onError(throwable)
    }) {
        repeatOnLifecycle(state) {
            flow.collect {
                block(it)
            }
        }
    }
}
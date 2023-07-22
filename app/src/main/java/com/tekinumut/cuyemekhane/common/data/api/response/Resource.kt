package com.tekinumut.cuyemekhane.common.data.api.response

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(val cuError: CuError) : Resource<Nothing>()
}
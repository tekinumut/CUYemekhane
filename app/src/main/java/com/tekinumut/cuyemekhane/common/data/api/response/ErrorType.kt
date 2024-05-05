package com.tekinumut.cuyemekhane.common.data.api.response

sealed interface ErrorType {
    data object General : ErrorType
    data object NoConnection : ErrorType
}
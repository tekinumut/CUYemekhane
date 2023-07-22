package com.tekinumut.cuyemekhane.common.data.api.response

sealed class ErrorType {
    object General : ErrorType()
    object NoConnection : ErrorType()
}
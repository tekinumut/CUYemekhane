package com.tekinumut.cuyemekhane.common.data.model.response

/**
 * Created by Umut Tekin on 15.01.2023.
 */
sealed class ErrorType {
    object General : ErrorType()
    object NoConnection : ErrorType()
}
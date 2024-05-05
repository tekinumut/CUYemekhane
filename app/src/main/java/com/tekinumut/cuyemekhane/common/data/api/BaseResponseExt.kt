package com.tekinumut.cuyemekhane.common.data.api

import com.tekinumut.cuyemekhane.common.data.api.response.CuError
import com.tekinumut.cuyemekhane.common.data.api.response.ErrorType
import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import java.net.UnknownHostException
import retrofit2.Response

private fun <T> Response<T>.handleResponse(): Resource<T> {
    return try {
        this.takeIf { it.isSuccessful }?.body()?.let {
            Resource.Success(it)
        } ?: run {
            Resource.Failure(
                CuError(errorType = ErrorType.General)
            )
        }
    } catch (e: Exception) {
        Resource.Failure(
            CuError(errorType = ErrorType.General)
        )
    }
}

suspend fun <T> handleApiCall(block: suspend () -> Response<T>): Resource<T> {
    return try {
        block.invoke().handleResponse()
    } catch (exception: UnknownHostException) {
        Resource.Failure(
            CuError(errorType = ErrorType.NoConnection)
        )
    } catch (exception: Exception) {
        Resource.Failure(
            CuError(errorType = ErrorType.General)
        )
    }
}
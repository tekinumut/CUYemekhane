package com.tekinumut.cuyemekhane.common.data.api

/**
 * Created by Umut Tekin on 15.01.2023.
 */
import com.tekinumut.cuyemekhane.common.data.model.response.CuError
import com.tekinumut.cuyemekhane.common.data.model.response.ErrorType
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import retrofit2.Response
import java.net.UnknownHostException

private fun <T> Response<T>.handleResponse(
): Resource<T> {
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
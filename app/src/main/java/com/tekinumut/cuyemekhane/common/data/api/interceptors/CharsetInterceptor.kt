package com.tekinumut.cuyemekhane.common.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class CharsetInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "text/plain;charset=UTF-8")
            .build()

        return chain.proceed(request)
    }
}
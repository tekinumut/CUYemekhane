package com.tekinumut.cuyemekhane.common.di

import com.tekinumut.cuyemekhane.BuildConfig
import com.tekinumut.cuyemekhane.common.data.api.ApiService
import com.tekinumut.cuyemekhane.common.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.NETWORK.BASE_ENDPOINT)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headers)
        .addInterceptor(encodingInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val headers = Interceptor { chain ->
        val newRequest = chain.request().newBuilder().apply {
            header("Content-Type", "text/plain;charset=UTF-8")
        }
        chain.proceed(newRequest.build())
    }

    private val encodingInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val mediaType = "text/html; charset=windows-1254".toMediaType()
        val requestBody = response.body?.bytes()?.toResponseBody(mediaType)
        response.newBuilder().body(requestBody).build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.NONE // TODO change with body
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}
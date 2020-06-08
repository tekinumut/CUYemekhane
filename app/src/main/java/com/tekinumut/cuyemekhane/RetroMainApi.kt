package com.tekinumut.cuyemekhane

import com.tekinumut.cuyemekhane.library.ConstantsOfWebSite
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface RetroMainApi {

    /**
     * Verilerin URL'nin html verisini çeker
     */
    @GET
    suspend fun getStringOfURL(@Url url: String): Response<String>

    /**
     * Verilerin resmi indirir.
     */
    @GET
    suspend fun getImageByte(@Url url: String): Response<ResponseBody>

    companion object {
        fun builder(): RetroMainApi {
            val client = OkHttpClient().newBuilder().addInterceptor(EncodingInterceptor()).build()
            return Retrofit.Builder().baseUrl(ConstantsOfWebSite.SourceURL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(RetroMainApi::class.java)
        }

        /**
         * Türkçe kelimeler yemek detayları bölümünde bozuk geliyor.
         * charset'i türkçe formata çevirerek bunu düzeltiyoruz.
         */
        class EncodingInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val response = chain.proceed(chain.request())
                val mediaType = MediaType.parse("text/html; charset=windows-1254")
                return response.body()?.let {
                    val modifiedBody = ResponseBody.create(mediaType, it.bytes())
                    response.newBuilder().body(modifiedBody).build()
                } ?: response.newBuilder().build()

            }
        }
    }
}
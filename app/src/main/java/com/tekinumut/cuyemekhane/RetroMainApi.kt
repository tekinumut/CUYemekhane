package com.tekinumut.cuyemekhane

import android.os.Build
import com.tekinumut.cuyemekhane.library.ConstantsOfWebSite
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

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
            //val client = OkHttpClient().newBuilder().addInterceptor(EncodingInterceptor()).build()
            return Retrofit.Builder().baseUrl(ConstantsOfWebSite.SourceURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(getOkHttpClient())
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
                val mediaType = "text/html; charset=windows-1254".toMediaType()
                val requestBody = response.body?.bytes()?.toResponseBody(mediaType)
                return response.newBuilder().body(requestBody).build()
            }
        }

        private fun getOkHttpClient(): OkHttpClient {
            val loggingBody = HttpLoggingInterceptor()
            loggingBody.setLevel(HttpLoggingInterceptor.Level.BODY)

            val clientBuilder = OkHttpClient.Builder().apply {
                connectTimeout(15, TimeUnit.SECONDS)
                readTimeout(15, TimeUnit.SECONDS)
                addInterceptor(getHeaders())
                addInterceptor(EncodingInterceptor())
                if (BuildConfig.DEBUG)
                    addInterceptor(loggingBody)
                // for API 23 and above -> we'll use network_security_config.xml
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
                    letUnsafeConnection()
            }
            return clientBuilder.build()
        }

        private fun getHeaders() = Interceptor { chain ->
            val newRequest = chain.request().newBuilder().apply {
                header("Content-Type", "text/plain;charset=UTF-8")
            }
            chain.proceed(newRequest.build())
        }

        /**
         * we trust every certificate to get rid of SSlHandleException
         * For greater than Api 23 we trust only erciyes_car certificate file
         * We initialize this certificate in network_security_config file
         * Note: I didn't find to way trust only erciyes_car certificate for below Api 23
         * and i didn't want to spend too much time to find solution
         *
         */
        private fun OkHttpClient.Builder.letUnsafeConnection(): OkHttpClient.Builder =
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts: Array<TrustManager> = arrayOf(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) = Unit

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) = Unit

                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
                )
                // Install the all-trusting trust manager
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
                this.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                this.hostnameVerifier { _, _ -> true }
                this
            } catch (e: Exception) {
                this
            }
    }
}
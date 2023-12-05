package com.example.tasklist.service.repository.remote

import com.example.tasklist.service.contants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        private lateinit var INSTANCE: Retrofit
        private const val BASE_URL = "http://devmasterteam.com/CursoAndroidAPI/"
        private var token: String = ""
        private var personKey: String = ""

        private fun getRetrofitInstance(): Retrofit {
            if (!::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class) {

                    val httpClient = OkHttpClient.Builder()

                    httpClient.addInterceptor(object : Interceptor {
                        override fun intercept(chain: Interceptor.Chain): Response {
                            val request = chain.request()
                                .newBuilder()
                                .addHeader(TaskConstants.HEADER.TOKEN_KEY, token)
                                .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                                .build()
                            return chain.proceed(request)
                        }
                    })

                    INSTANCE = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }

            return INSTANCE
        }

        fun <T> getService(serviceClass: Class<T>): T {
            return getRetrofitInstance().create(serviceClass)
        }

        fun addHeaders(tokenValue: String, personKeyValue: String) {
            token = tokenValue
            personKey = personKeyValue
        }
    }
}
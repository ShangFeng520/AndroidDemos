package com.example.sunnyweather.logic.network

import com.example.sunnyweather.logic.model.PlaceResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"
    private var retrofit:Retrofit
    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    internal inline fun <reified T> create():T = retrofit.create(T::class.java)

//    fun <T> create():T = create(T::class.java)
}
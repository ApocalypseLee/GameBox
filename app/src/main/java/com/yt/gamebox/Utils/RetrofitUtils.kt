package com.yt.gamebox.Utils

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.yt.gamebox.Services.RequestService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils {
    private fun getApiService(): RequestService {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RequestService::class.java)
    }


    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().apply { addInterceptor(interceptor) }.build()

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    fun getGameDatas() = getApiService().getGameDatas()
}
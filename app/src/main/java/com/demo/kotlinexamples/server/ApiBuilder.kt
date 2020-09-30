package com.demo.kotlinexamples.server

import com.demo.kotlinexamples.server.ApiClient.Companion.BASE_URL
import dagger.Module
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiBuilder {

    private val client = OkHttpClient
        .Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(ApiClient::class.java)

    fun buildService(): ApiClient {
        return retrofit
    }
}
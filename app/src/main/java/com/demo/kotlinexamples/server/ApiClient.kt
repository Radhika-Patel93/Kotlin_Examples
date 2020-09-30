package com.demo.kotlinexamples.server

import com.demo.kotlinexamples.model.TimeModel
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiClient {

    companion object {
        const val BASE_URL = "http://worldtimeapi.org/"
    }

    @GET("api/timezone/Europe/London")
    fun getTime() : Observable<TimeModel>
}
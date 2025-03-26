package com.example.connectinternet.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import java.io.File

interface ApiConnect {
    @Headers("Authorization: Api-key AQVN3SBzhU-ix0y0-vo6WfrEXmlo7f04h_lwVFek",
        "Content-Type: application/octet-stream")
    @GET("v1/stt:recognize?topic=general")
    fun getWord(data: File): Call<Result>
}
data class Result(
    val result:String
)
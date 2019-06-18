package com.example.w09c

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ChuckNorrisAPI {
    @GET("/jokes/{random}")
    fun findJoke(@Path("random") random : String) : Call<ChuckNorrisDTO>
}
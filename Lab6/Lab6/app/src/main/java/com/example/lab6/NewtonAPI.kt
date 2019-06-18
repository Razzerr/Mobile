package com.example.lab6

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NewtonAPI {
    @GET("{op}/{exp}")
    fun solve(@Path("op") operation: String, @Path("exp") expression: String): Call<NewtonDTO>
//    fun solveZeroes(@Path("op") operation: String, @Path("exp") expression: String): Call<NewtonResArrayDTO>
}
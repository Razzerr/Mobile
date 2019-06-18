package com.example.w09c

data class ChuckNorrisValue(
    var id : Int,
    var joke : String,
    val categories : List<Any>) {
}
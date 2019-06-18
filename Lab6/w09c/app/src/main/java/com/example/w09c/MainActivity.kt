package com.example.w09c

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.icndb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val chuck = retrofit.create(ChuckNorrisAPI::class.java)
        val call = chuck.findJoke("random")

        call.enqueue( object : Callback<ChuckNorrisDTO> {
            override fun onFailure(call: Call<ChuckNorrisDTO>, t: Throwable) {
                Log.d("am2019", "ups")
            }

            override fun onResponse(call: Call<ChuckNorrisDTO>, response: Response<ChuckNorrisDTO>) {
                val body = response.body()
                textView.text = body!!.value.joke
            }

        })

    }
}

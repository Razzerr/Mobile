package com.example.w09b

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val json = "{\"name\": \"android\", \"id\": 123,  \"balance\": 0.5}"

        val gson = Gson()

        val bankAccount = gson.fromJson(json, BankAccount::class.java)

    }
}

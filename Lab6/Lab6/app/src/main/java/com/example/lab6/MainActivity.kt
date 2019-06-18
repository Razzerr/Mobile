package com.example.lab6

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(){
    val retrofit = Retrofit.Builder()
        .baseUrl("http://156.17.7.48:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val newton = retrofit.create(NewtonAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun btnClick(v: View?){
        val expr : String = edExp.text.toString()
        val call = when (v!!.id) {
            R.id.btnSimplify -> newton.solve("simplify", expr)
            R.id.btnFactor -> newton.solve("factor", expr)
            R.id.btnDerive -> newton.solve("derive", expr)
            R.id.btnIntegrate -> newton.solve("integrate", expr)
            R.id.btnFTangent -> newton.solve("tangent", expr)
            R.id.btnArea -> newton.solve("area", expr)
            R.id.btnCosine -> newton.solve("cos", expr)
            R.id.btnSine -> newton.solve("sin", expr)
            R.id.btnTangent -> newton.solve("tan", expr)
            R.id.btnInvCos -> newton.solve("arccos", expr)
            R.id.btnInvSin -> newton.solve("arcsin", expr)
            R.id.btnInvTan -> newton.solve("arctan", expr)
            R.id.btnAbs -> newton.solve("abs", expr)
            R.id.btnLog -> newton.solve("log", expr)
            R.id.btnZeroes -> newton.solve("zeroes", expr)
            else -> newton.solve("simplify", expr)
        }


        call.enqueue(object : Callback<NewtonDTO> {
            override fun onFailure(call: Call<NewtonDTO>, t: Throwable) {
                Log.d("am2019", "ups")
            }

            override fun onResponse(call: Call<NewtonDTO>, response: Response<NewtonDTO>) {
                val body = response.body()
                edRes.text = body!!.result.toString()
            }
        })
    }
}
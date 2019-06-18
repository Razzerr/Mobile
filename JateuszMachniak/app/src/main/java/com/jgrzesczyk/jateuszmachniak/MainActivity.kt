package com.jgrzesczyk.jateuszmachniak

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.graphics.PorterDuff
import android.content.res.ColorStateList
import android.support.annotation.RequiresApi
import android.widget.Switch


class MainActivity : AppCompatActivity() {

    var prefs : Prefs? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = Prefs(this)

        val myLocale = Locale(prefs!!.locale)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLayout(prefs!!.layout)

        if(prefs!!.remember) {
            if (tryLogin(prefs!!.login, prefs!!.password, prefs!!.server)) loginSuccess() else loginFailure()
        }
        remember.text = (remember.text.toString() + "    ")
        editText3.setText(prefs!!.server)



    }

    fun login(view: View?){
        val login = editText.text.toString()
        val password = editText2.text.toString()
        val server = editText3.text.toString()
        prefs!!.login = login
        prefs!!.password = password
        prefs!!.server = server
        prefs!!.remember = remember.isChecked
        var success = false
        val thread = Thread(Runnable {
            try {
                CloudClient.login(login, password, server)
                success = CloudClient.validCredentials()
            } catch (e: Exception) { }
        })
        thread.start()
        thread.join()
        if (success) loginSuccess() else loginFailure()
    }

    private fun tryLogin(login: String, password: String, server : String): Boolean{
        var success = false
        val thread = Thread(Runnable {
            try {
                CloudClient.login(prefs!!.login, prefs!!.password, prefs!!.server)
                success = CloudClient.validCredentials()
            } catch (e: Exception) { }
        })
        thread.start()
        thread.join()
        return success
    }

    private fun loginSuccess(){
        incorrect.visibility = View.GONE
        val fileManager = Intent(this, FileManagerActivity::class.java)
        finish()
        startActivity(fileManager)
    }

    private fun loginFailure(){
        editText2.setText("") //set Password to none
        incorrect.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setLayout(layout: String) {
        if (layout == "Light") {
            bg_main.setBackgroundColor(Color.WHITE)
            logo_main.setTextColor(Color.BLACK)
            textView.setTextColor(Color.BLACK)
            textView2.setTextColor(Color.BLACK)
            textView3.setTextColor(Color.BLACK)
            remember.setTextColor(Color.BLACK)
            incorrect.setTextColor(Color.RED)
            remember.setLinkTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editText.background = this.getDrawable(R.drawable.button_border_white)
                editText.setTextColor(Color.BLACK)
                editText2.background = this.getDrawable(R.drawable.button_border_white)
                editText2.setTextColor(Color.BLACK)
                editText3.background = this.getDrawable(R.drawable.button_border_white)
                editText3.setTextColor(Color.BLACK)
                button.background = this.getDrawable(R.drawable.button_border_white)
                button.setTextColor(Color.BLACK)
            }

            changeSwitchButton(Color.BLACK, Color.DKGRAY, remember)

        } else if(layout == "Contrast") {
            bg_main.setBackgroundColor(Color.BLUE)
            logo_main.setTextColor(Color.YELLOW)
            textView.setTextColor(Color.YELLOW)
            textView2.setTextColor(Color.YELLOW)
            textView3.setTextColor(Color.YELLOW)
            remember.setTextColor(Color.YELLOW)
            incorrect.setTextColor(Color.YELLOW)
            remember.setLinkTextColor(Color.YELLOW)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editText.background = this.getDrawable(R.drawable.button_border_blue)
                editText.setTextColor(Color.YELLOW)
                editText2.background = this.getDrawable(R.drawable.button_border_blue)
                editText2.setTextColor(Color.YELLOW)
                editText3.background = this.getDrawable(R.drawable.button_border_blue)
                editText3.setTextColor(Color.YELLOW)
                button.background = this.getDrawable(R.drawable.button_border_blue)
                button.setTextColor(Color.YELLOW)
            }

            changeSwitchButton(Color.YELLOW, Color.RED, remember)

        } else {
            bg_main.setBackgroundColor(Color.BLACK)
            logo_main.setTextColor(Color.WHITE)
            textView.setTextColor(Color.WHITE)
            textView2.setTextColor(Color.WHITE)
            textView3.setTextColor(Color.WHITE)
            remember.setTextColor(Color.WHITE)
            incorrect.setTextColor(Color.RED)
            remember.setLinkTextColor(Color.WHITE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editText.background = this.getDrawable(R.drawable.button_border)
                editText.setTextColor(Color.WHITE)
                editText2.background = this.getDrawable(R.drawable.button_border)
                editText2.setTextColor(Color.WHITE)
                editText3.background = this.getDrawable(R.drawable.button_border)
                editText3.setTextColor(Color.WHITE)
                button.background = this.getDrawable(R.drawable.button_border)
                button.setTextColor(Color.WHITE)
            }
            changeSwitchButton(Color.WHITE, Color.LTGRAY, remember)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeSwitchButton(color1: Int, color2: Int, switch: Switch) {
        val thumbStates = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_checked), intArrayOf()),
            intArrayOf(color1, color1, color1)
        )
        switch.thumbTintList = thumbStates

        if (Build.VERSION.SDK_INT >= 24) {
            val trackStates = ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf()),
                intArrayOf(color2, color2)
            )
            switch.trackTintList = trackStates
            switch.trackTintMode = PorterDuff.Mode.OVERLAY
        }
    }
}

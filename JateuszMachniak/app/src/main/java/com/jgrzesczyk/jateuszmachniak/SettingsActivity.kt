package com.jgrzesczyk.jateuszmachniak

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


class SettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var prefs: Prefs? = null

    private lateinit var themes : MutableMap<String,String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = Prefs(this)
        setLayout(prefs!!.layout)

        themes = mutableMapOf(
            "Dark" to getString(R.string.theme_dark),
            "Light" to getString(R.string.theme_light),
            "Contrast" to getString(R.string.theme_contrast)
        )

        val languageSpinner: Spinner = languageSpinner
        val themeSpinner: Spinner = themeSpinner

         btnLogout.setOnClickListener{
            prefs!!.remember = false
            prefs!!.login = ""
            prefs!!.password = ""
            val loginscreen = Intent(this, MainActivity::class.java)
            finish()
             loginscreen.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(loginscreen)
        }

        ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSpinner.adapter = adapter

        }

        val themeAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, themes.values.toTypedArray()
        )
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSpinner.adapter = themeAdapter

        setSpinText(themeSpinner, themes[prefs!!.layout])

        languageSpinner.onItemSelectedListener = this
        themeSpinner.onItemSelectedListener = this

    }

    private fun changeLocale(context: Context, lang: String) {
        setLocale(lang)
        prefs!!.reload = true
        finish()
        startActivity(this.intent)
    }


    private fun setLocale(lang: String) {
        prefs!!.locale = lang
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

    private fun changeLayout(lay: String){
        prefs!!.layout = lay
        setLayout(lay)
        prefs!!.reload = true
    }


    private fun setLayout(layout: String){
        if (layout == "Light") {
            bg_settings.setBackgroundColor(Color.WHITE)
            logo_settings.setTextColor(Color.BLACK)

            textview1.setTextColor(Color.BLACK)
            textview2.setTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnLogout.background = this.getDrawable(R.drawable.button_border_white)
                btnLogout.setTextColor(Color.BLACK)
            }

        } else if(layout == "Contrast") {
            bg_settings.setBackgroundColor(Color.BLUE)
            logo_settings.setTextColor(Color.YELLOW)

            textview1.setTextColor(Color.YELLOW)
            textview2.setTextColor(Color.YELLOW)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnLogout.background = this.getDrawable(R.drawable.button_border_blue)
                btnLogout.setTextColor(Color.YELLOW)
            }
        } else {
            bg_settings.setBackgroundColor(Color.BLACK)
            logo_settings.setTextColor(Color.WHITE)

            textview1.setTextColor(Color.WHITE)
            textview2.setTextColor(Color.WHITE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnLogout.background = this.getDrawable(R.drawable.button_border)
                btnLogout.setTextColor(Color.WHITE)
            }
        }
    }

    private fun getKey(themes : MutableMap<String,String>, value : String) : String?{
        for (i in themes.keys) {
            if (themes[i] == value){
                return i
            }
        }
        return null
    }

    private fun setSpinText(spin: Spinner, text: String?) {
        for (i in 0 until spin.adapter.count) {
            if (spin.adapter.getItem(i).toString() == text) {
                spin.setSelection(i)
            }
        }

    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, this.intent)
        finish()
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        if (parent == languageSpinner){
            val loc = when (parent.getItemAtPosition(pos)) {
                "English" -> "en"
                "Deutsch" -> "de"
                "Polski" -> "pl"
                "日本語" -> "ja"
                else -> "en"
            }

            if (prefs!!.locale != loc){
                changeLocale(this, loc)
            }
        }
        else if (parent == themeSpinner){
            val themeCode = getKey(themes, parent.getItemAtPosition(pos).toString())
            val lay = when (themeCode) {
                null -> "Dark"
                in arrayOf("Dark", "Light", "Contrast") -> themeCode
                else -> "Dark"
            }

            if(prefs!!.layout != lay){
                changeLayout(lay)
            }
        }


    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

}


@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.jgrzesczyk.jateuszmachniak

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources

class Prefs(context: Context) {
    val PREFS_FILENAME = "MACHNIAK_PREFS"
    val LOCALE = "locale"
    val LAYOUT = "layout"
    val LOGIN = "login"
    val PASSWORD = "password"
    val SERVER = "server_address"
    val RELOAD = "reload_needed"
    val REMEMBER = "remember_user"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var locale: String
        get() = prefs.getString(LOCALE, "en")
        set(value) = prefs.edit().putString(LOCALE, value).apply()

    var layout: String
        get() = prefs.getString(LAYOUT, "Dark")
        set(value) = prefs.edit().putString(LAYOUT, value).apply()

    var login: String
        get() = prefs.getString(LOGIN, "")
        set(value) = prefs.edit().putString(LOGIN, value).apply()

    var password: String
        get() = prefs.getString(PASSWORD, "")
        set(value) = prefs.edit().putString(PASSWORD, value).apply()

    var server: String
        get() = prefs.getString(SERVER, "localhost")
        set(value) = prefs.edit().putString(SERVER, value).apply()

    var reload: Boolean
        get() = prefs.getBoolean(RELOAD, false)
        set(value) = prefs.edit().putBoolean(RELOAD, value).apply()

    var remember: Boolean
        get() = prefs.getBoolean(REMEMBER, false)
        set(value) = prefs.edit().putBoolean(REMEMBER, value).apply()
}
package com.example.welfast.Base

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private lateinit var prefs: SharedPreferences

    private const val PREFS_NAME = "params"

    const val ID_USER = "id_user"
    const val TOKEN = "token"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String): String? {
        return prefs.getString(key, "")
    }
    fun readBool(key: String): Boolean? {
        return prefs.getBoolean(key,false)
    }

    fun read(key: String, value: Long): Long? {
        return prefs.getLong(key, value)
    }

    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, value)
            apply()
        }
    }
    fun writeBool(key: String, value: Boolean) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putBoolean(key, value)
            apply()
        }
    }

    fun write(key: String, value: Long) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putLong(key, value)
            apply()
        }
    }

    fun clearPref()
    {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        prefsEditor.clear()
        prefsEditor.apply()

    }
}
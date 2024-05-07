package com.pabloat.GameHubConnect.viewmodel


import android.content.Context

class PreferenceUtils {
    private val PREF_NAME = "MyPrefs"
    private val KEY_REMEMBER_ME = "rememberMe"
    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"

    fun getRememberMeState(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)
    }

    fun saveRememberMeState(state: Boolean, context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_REMEMBER_ME, state).apply()
    }

    fun saveUserCredentials(email: String, password: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASSWORD, password)
            .apply()
    }

    fun getSavedEmail(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    fun getSavedPassword(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_PASSWORD,null)
    }

}
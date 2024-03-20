package com.sehatin.ittp

import android.content.Context

internal class SettingPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "setting_pref"
        private const val NAME = "name"
        private const val NIM = "nim"
        private const val GENDER = "gender"
        private const val EMAIL = "email"

        //        private const val PASSWORD = "password"
        private const val AGE = "age"
        private const val PHONE_NUMBER = "phone"
        private const val THEME = "theme"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setSetting(value: SettingModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putInt(NIM, value.nim)
        editor.putBoolean(GENDER, value.gender)
        editor.putString(EMAIL, value.email)
//        editor.putString(PASSWORD, value.password)
        editor.putInt(AGE, value.age)
        editor.putString(PHONE_NUMBER, value.phoneNumber)
        editor.putBoolean(THEME, value.isDarkTheme)
        editor.apply()
    }

    fun getSetting(): SettingModel {
        val model = SettingModel()
        model.name = preferences.getString(NAME, "")
        model.nim = preferences.getInt(NIM, 0)
        model.gender = preferences.getBoolean(GENDER, false)
        model.email = preferences.getString(EMAIL, "")
//        model.password = preferences.getString(PASSWORD, "")
        model.age = preferences.getInt(AGE, 0)
        model.phoneNumber = preferences.getString(PHONE_NUMBER, "")
        model.isDarkTheme = preferences.getBoolean(THEME, false)
        return model
    }
}
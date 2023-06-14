package com.example.loginpage1

import android.content.Context
import android.content.SharedPreferences
import com.example.loginpage1.Constants.Companion.PREFS_NAME


class SharedPrefUtils(private var mContext: Context? = null) {

    fun sharedPreferenceExist(key: String?): Boolean {
        val prefs = mContext!!.getSharedPreferences(PREFS_NAME, 0)
        return !prefs.contains(key)
    }

    fun setInt(key: String?, value: Int) {
        val prefs = mContext!!.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }


    fun getInt(key: String?): Int {
        val prefs = mContext!!.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getInt(key, 0)
    }

    fun setStr(key: String?, value: String?) {
        val prefs = mContext!!.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStr(key: String?): String? {
        val prefs = mContext!!.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getString(key, "DNF")
    }

    fun setBool(key: String?, value: Boolean) {
        val prefs = mContext!!.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String?): Boolean {
        val prefs = mContext!!.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean(key, false)
    }

    fun clearPref() {
        val preferences: SharedPreferences =
            mContext!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}
package com.example.loginpage1

class Constants {
    companion object {
        var REGISTERED = 1
        var OTP_VERIFIED = 2
        var isUserRegistered = "isUserRegistered"
        var WHICH_SCREEN = "isWhichScreen"
        var USER_NAME = "username"
        val PREFS_NAME = "DemoSharedPref"

        //Database Statics
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "UserManager.db"
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PROFILE = "Profile"
    }
}
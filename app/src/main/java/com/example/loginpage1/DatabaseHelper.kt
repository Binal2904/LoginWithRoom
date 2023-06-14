package com.example.loginpage1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.loginpage1.Constants.Companion.COLUMN_ID
import com.example.loginpage1.Constants.Companion.COLUMN_PASSWORD
import com.example.loginpage1.Constants.Companion.COLUMN_PROFILE
import com.example.loginpage1.Constants.Companion.COLUMN_USERNAME
import com.example.loginpage1.Constants.Companion.DATABASE_NAME
import com.example.loginpage1.Constants.Companion.DATABASE_VERSION
import com.example.loginpage1.Constants.Companion.TABLE_NAME

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE =
            ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COLUMN_USERNAME TEXT,$COLUMN_PASSWORD TEXT,$COLUMN_PROFILE TEXT)")
        db.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun registerUser(
        username: String, password: String, imageUrl: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_PROFILE, imageUrl)
        return db.insert(TABLE_NAME, null, values)
    }

    fun loginUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor: Cursor = db.query(
            TABLE_NAME, null, selection, selectionArgs, null, null, null
        )
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    fun getUserData(username: String): User? {
        val db = this.readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor: Cursor = db.query(
            TABLE_NAME, null, selection, selectionArgs, null, null, null
        )
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val profilePhoto = cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE))
            User(id, username, password, profilePhoto)
        } else {
            null
        }
    }

    fun deleteUser(username: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_USERNAME = ?"
        val whereArgs = arrayOf(username)
        val rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs)
        return rowsDeleted > 0
    }
}
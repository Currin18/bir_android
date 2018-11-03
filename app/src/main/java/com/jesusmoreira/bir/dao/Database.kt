package com.jesusmoreira.bir.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.jesusmoreira.bir.dao.question.IQuestionScheme
import com.jesusmoreira.bir.dao.question.QuestionDao

class Database(val context: Context) {
    companion object {
        const val TAG = "Database"
        const val DATABASE_NAME = "database.db"
        const val DATABASE_VERSION = 1

        private class DatabaseHelper(
                context: Context
        ): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
            override fun onCreate(db: SQLiteDatabase?) {
                db?.execSQL(IQuestionScheme.TABLE_CREATE)
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                Log.w(TAG, "Upgrading database from version $oldVersion to $newVersion which destroys all old data")

                db?.execSQL("DROP TABLE IF EXISTS ${IQuestionScheme.TABLE_NAME}")
                onCreate(db)
            }
        }
    }

    private val dbHelper = DatabaseHelper(context)
    var questionDao: QuestionDao? = null

    fun open(): Database {
        val db = dbHelper.writableDatabase
        questionDao = QuestionDao(db)
        return this
    }

    fun close() {
        dbHelper.close()
    }
}
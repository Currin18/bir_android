package com.jesusmoreira.bir.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.lang.Exception

abstract class DbContentProvider<T>(var db: SQLiteDatabase) {
    companion object {
        val TAG = "Database"
    }

    fun insert(tableName: String, values: ContentValues) =
            db.insert(tableName, null, values)

    fun insertBulk(tableName: String, values: List<T>) {
        if(values.isNotEmpty()) {
            db.beginTransaction()
            try {
                values.forEach {
                    db.insert(tableName, null, entityToContentValues(it))
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            } finally {
                db.endTransaction()
            }
        }
    }

    fun update(tableName: String, values: ContentValues, selection: String, selectionArgs: Array<String>) =
            db.update(tableName, values, selection, selectionArgs)

    fun delete(tableName: String, selection: String, selectionArgs: Array<String>) =
            db.delete(tableName, selection, selectionArgs)

    protected abstract fun cursorToEntity(cursor: Cursor): T
    protected abstract fun entityToContentValues(entity: T): ContentValues

    fun query(tableName: String, columns: Array<String>, selection: String?,
              selectionArgs: Array<String>?, orderBy: String?): Cursor =
            db.query(tableName, columns, selection,
                selectionArgs, null, null, orderBy)

    fun query(tableName: String, columns: Array<String>, selection: String?,
              selectionArgs: Array<String>?, orderBy: String?, limit: String?): Cursor =
            db.query(tableName, columns, selection,
                    selectionArgs, null, null, orderBy, limit)

    fun query(tableName: String, columns: Array<String>, selection: String?,
              selectionArgs: Array<String>?, groupBy: String?, having: String?,
              orderBy: String?, limit: String?): Cursor =
            db.query(tableName, columns, selection,
                    selectionArgs, groupBy, having, orderBy, limit)

    fun rawQuery(sql: String, selectionArgs: Array<String>): Cursor = db.rawQuery(sql, selectionArgs)
}
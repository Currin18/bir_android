package com.jesusmoreira.bir.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesUtils {
    private const val PREFERENCES_NAME = "birPreferences"
    private const val KEY = "bir.utils.PreferenceUtils"
    private const val KEY_DB_LAST_UPDATE = "$KEY.DB_LAST_UPDATE"

    private fun getPreferences(context: Context): SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private fun getEditor(context: Context): SharedPreferences.Editor = getPreferences(context).edit()

    fun getDbLastUpdate(context: Context): Long = getPreferences(context).getLong(KEY_DB_LAST_UPDATE, 0)
    fun setDbLastUpdate(context: Context, value: Long): Any = getEditor(context).putLong(KEY_DB_LAST_UPDATE, value).commit()
}
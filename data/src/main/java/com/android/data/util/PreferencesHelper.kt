package com.android.data.util

interface PreferencesHelper {
    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean
    fun getIntValue(key: String): Int
    fun getIntValue(key: String, defaultValue: Int): Int
    fun getLongValue(Key: String): Long
    fun getLongValue(Key: String, defaultValue: Long): Long
    fun getStringValue(key: String): String?
    fun getStringValue(key: String, defaultValue: String): String?
    fun saveBooleanValue(key: String, value: Boolean)
    fun saveIntValue(key: String, value: Int)
    fun saveStringValue(key: String, value: String)
    fun saveLongValue(key: String, value: Long)
    fun clear()
}
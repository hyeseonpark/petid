package com.android.petid.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.android.data.util.PreferencesHelper
import javax.inject.Inject


/**
 * App 내 공통 SharedPreference
 */
class PreferencesControl @Inject constructor(
    private val context: Context
): PreferencesHelper {
    private var TAG = "PreferencesControl"

    /**
     * 저장된 boolean value return
     * @param context
     * @param title
     * @param flag
     * @return
     */
    override fun getBooleanValue(key: String, flag: Boolean): Boolean {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getBoolean(key, flag)
    }

    /**
     * 저장된 Int value return
     * @param context
     * @param title
     * @return long value
     */
    override fun getIntValue(key: String): Int {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getInt(key, 0)
    }

    /**
     * 저장된 Int value return
     * @param context
     * @param title
     * @return long value
     */
    override fun getIntValue(key: String, defaultValue: Int): Int {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getInt(key, defaultValue)
    }
    /**
     * 저장된 Int value return
     * @param context
     * @param title
     * @return long value
     */
    override fun getLongValue(Key: String): Long {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getLong(Key, 0)
    }

    /**
     * 저장된 Int value return
     * @param context
     * @param title
     * @return long value
     */
    override fun getLongValue(Key: String, defaultValue: Long): Long {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getLong(Key, defaultValue)
    }

    /**
     * 저장된 String value return
     * @param context
     * @param title
     * @return
     */
    override fun getStringValue(key: String): String? {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            return pref.getString(key, "")
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }

        return null
    }

    /**
     * 저장된 String value return
     * @param context
     * @param title
     * @return
     */
    override fun getStringValue(key: String, defaultValue: String): String? {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        var result = pref.getString(key, defaultValue)

        if (TextUtils.isEmpty(result)) {
            result = defaultValue
        }

        return result
    }

    /**
     * boolean value 저장하기
     * @param context
     * @param title
     * @param flag
     */
    override fun saveBooleanValue(key: String, flag: Boolean) {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean(key, flag)
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     * Int value 저장하기
     * @param context
     * @param title
     * @param value
     */
    override fun saveIntValue(key: String, value: Int) {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt(key, value)
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     * String value 저장하기
     * @param context
     * @param title
     */
    override fun saveStringValue(key: String, value: String) {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(key, value)
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     * String value 저장하기
     * @param context
     * @param title
     */
    override fun saveLongValue(key: String, value: Long) {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putLong(key, value)
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }
}

package com.petid.petid.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.petid.data.util.PreferencesHelper
import javax.inject.Inject


/**
 * App 내 공통 SharedPreference
 */
class PreferencesControl @Inject constructor(
    private val context: Context
): PreferencesHelper {
    private val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    /**
     * 저장된 boolean value return
     */
    override fun getBooleanValue(key: String, flag: Boolean): Boolean {
        return pref.getBoolean(key, flag)
    }

    /**
     * 저장된 Int value return
     */
    override fun getIntValue(key: String): Int {
        return pref.getInt(key, -1)
    }

    /**
     * 저장된 Int value return: defaultValue 설정
     */
    override fun getIntValue(key: String, defaultValue: Int): Int {
        return pref.getInt(key, defaultValue)
    }

    /**
     * 저장된 Long value return
     */
    override fun getLongValue(key: String): Long {
        return pref.getLong(key, 0)
    }

    /**
     * 저장된 Long value return: defaultValue 설정
     */
    override fun getLongValue(key: String, defaultValue: Long): Long {
        return pref.getLong(key, defaultValue)
    }

    /**
     * 저장된 String value return
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
     * 저장된 String value return: defaultValue 설정
     */
    override fun getStringValue(key: String, defaultValue: String): String? {
        var result = pref.getString(key, defaultValue)

        if (TextUtils.isEmpty(result)) {
            result = defaultValue
        }

        return result
    }

    /**
     * boolean value 저장
     */
    override fun saveBooleanValue(key: String, flag: Boolean) {
        try {
            val editor = pref.edit()
            editor.putBoolean(key, flag)
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     * Int value 저장
     */
    override fun saveIntValue(key: String, value: Int) {
        try {
            val editor = pref.edit()
            editor.putInt(key, value)
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     * String value 저장
     */
    override fun saveStringValue(key: String, value: String) {
        try {
            val editor = pref.edit()
            editor.putString(key, value)
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     * Long value 저장
     */
    override fun saveLongValue(key: String, value: Long) {
        try {
            val editor = pref.edit()
            editor.putLong(key, value)
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    override fun clear() {
        try {
            val editor = pref.edit()
            editor.clear()
            editor.apply()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }
}

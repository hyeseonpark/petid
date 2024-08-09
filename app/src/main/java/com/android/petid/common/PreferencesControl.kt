package com.android.petid.common

import android.content.Context
import androidx.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONException


/**
 * App 내 공통 SharedPreference
 */
object PreferencesControl {
    private const val TAG = "PreferencesControl"

    /**
     * 저장된 boolean value return
     * @param context
     * @param title
     * @param flag
     * @return
     */
    fun getBooleanValue(context: Context, title: String?, flag: Boolean): Boolean {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getBoolean(title, flag)
    }

    /**
     * 저장된 Int value return
     * @param context
     * @param title
     * @return long value
     */
    fun getIntValue(context: Context, title: String?): Int {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getInt(title, 0)
    }

    /**
     *
     * @param context
     * @param title
     * @param returnValue
     * @return
     */
    fun getIntValue(context: Context, title: String?, returnValue: Int): Int {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getInt(title, returnValue)
    }

    /**
     * 저장된 Int value return
     * @param context
     * @param title
     * @return long value
     */
    fun getLongValue(context: Context, title: String?): Long {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getLong(title, 0)
    }

    /**
     * 저장된 String value return
     * @param context
     * @param title
     * @return
     */
    fun getStringValue(context: Context, title: String?): String? {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            return pref.getString(title, "")
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
    fun getStringValue(context: Context, title: String?, defaultValue: String?): String? {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        var result = pref.getString(title, "")

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
    fun saveBooleanValue(context: Context, title: String?, flag: Boolean) {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean(title, flag)
            editor.commit()
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
    fun saveIntValue(context: Context, title: String?, value: Int) {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt(title, value)
            editor.commit()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     * String value 저장하기
     * @param context
     * @param title
     */
    fun saveStringValue(context: Context, title: String?, value: String?) {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(title, value)
            editor.commit()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     * String value 저장하기
     * @param context
     * @param title
     */
    fun saveLongValue(context: Context, title: String?, value: Long) {
        try {
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putLong(title, value)
            editor.commit()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     *
     * @param context
     * @param key
     * @param values
     */
    fun saveStringArrayValue(context: Context, key: String?, values: ArrayList<String?>) {
        try {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()

            val a = JSONArray()

            for (i in values.indices) {
                a.put(values[i])
            }

            if (!values.isEmpty()) {
                editor.putString(key, a.toString())
            } else {
                editor.putString(key, null)
            }

            editor.commit()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.localizedMessage)
        }
    }

    /**
     *
     * @param context
     * @param key
     * @return
     */
    fun getStringArrayValue(context: Context, key: String?): ArrayList<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val json = prefs.getString(key, null)
        val urls = ArrayList<String>()

        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    urls.add(url)
                }
            } catch (e: JSONException) {
                Log.e(TAG, "Exception: " + e.localizedMessage)
            }
        }

        return urls
    }
}

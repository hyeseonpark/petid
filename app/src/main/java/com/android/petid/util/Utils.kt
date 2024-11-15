package com.android.petid.util

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utils {
}

val genderCharToString: (char: Char) -> String = {char -> if(char == 'M') "남" else "여" }
val booleanCharToSign: (char: Char) -> String = {char -> if(char == 'Y') "O" else "X"}

fun setStyleSpan(context: Context, content: String, word: String, color: Int, bold: Boolean = false) : SpannableString {
    val spannableString = SpannableString(content)

    val wordContent = String.format(word)
    val start = content.indexOf(wordContent)
    val end = start + wordContent.length

    spannableString.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, color)),
        start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    if (bold)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return spannableString
}

fun setBoldSpan(context: Context, content: String, word: String, color: Int) : SpannableString {
    val spannableString = SpannableString(content)

    val wordContent = String.format(word)
    val start = content.indexOf(wordContent)
    val end = start + wordContent.length


    return spannableString
}

/**
 * 오늘 날짜
 */
 fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}

fun formatDateToISO8601(date: Date): String {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC") // UTC 시간대로 설정
    return isoFormat.format(date)
}
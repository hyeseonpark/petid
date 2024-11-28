package com.android.petid.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
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
 * 오늘 날짜(yyyy-MM-dd)
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


/**
 *  Bitmap to File
 */
fun bitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
    // 임시 파일 경로
    val file = File(context.cacheDir, fileName)
    // 파일에 Bitmap을 저장
    val fileOutputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    fileOutputStream.flush()
    fileOutputStream.close()
    return file
}

/**
 * generate random name
 */
fun getRandomFileName(dir: String): String {
    val characters = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val randomName = (1..10)
        .map { characters.random() }
        .joinToString("")
    return "$dir/$randomName.jpg"
}

/**
 * 키보드 숨기기 및 editText Focus clear
 */
fun Activity.hideKeyboardAndClearFocus() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isAcceptingText) {
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    if (currentFocus is EditText) {
        currentFocus?.clearFocus()
    }
}
package com.android.petid.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.android.petid.R
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utils {
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
}

object ProgressDialogUtil {
    private var progressDialog: Dialog? = null

    fun show(context: Context) {
        if (progressDialog?.isShowing == true) return

        progressDialog = Dialog(context).apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(
                ProgressBar(getGlobalContext()).apply {
                    progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(getGlobalContext(), R.color.petid_clear_blue))}
            )
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
        }
    }

    fun cancel() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}
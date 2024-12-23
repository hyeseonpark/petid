package com.petid.petid.util

import android.app.DatePickerDialog
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
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.petid.petid.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
 * 달력 표시
 */
fun showDatePicker(editText: EditText, context: Context) {
    val currentDate = editText.text.toString().ifEmpty { getCurrentDate() }
    val dateParts = currentDate.split("-")

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.DatePickerDialogTheme,
        { _, year, monthOfYear, dayOfMonth ->
            editText.setText(
                String.format(Locale.KOREA, "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
            )
        },
        dateParts[0].toInt(),
        dateParts[1].toInt() - 1,
        dateParts[2].toInt()
    )
    datePickerDialog.apply {
        show()
        getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
            context.resources.getColor(R.color.petid_clear_blue, null))
        getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
            context.resources.getColor(R.color.petid_subtitle, null))
    }
}

/**
 * 생년월일을 기준으로 현재 나이 계산
 */
fun calculateAge(birthDateString: String): Int {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val birthDate = sdf.parse(birthDateString) ?: return -1 // 파싱 실패 시 -1 반환

    // 현재 날짜
    val today = Calendar.getInstance()

    // 생일 날짜
    val birthCalendar = Calendar.getInstance().apply {
        time = birthDate
    }

    var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR) + 1

    return age
}


/**
 * progress Dialog init
 */
object ProgressDialogUtil {
    private var progressDialog: Dialog? = null

    fun show(context: Context) {
        if (progressDialog?.isShowing == true) return

        progressDialog = Dialog(context).apply {
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }

            setContentView(
                ProgressBar(context).apply {
                    progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.petid_clear_blue))}
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
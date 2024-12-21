package com.android.petid.util

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.android.petid.R
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.util.Utils.getCurrentDate
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
 * progress Dialog init
 */
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

/**
 * 전화번호 하이픈 추가
 */
fun EditText.addPhoneNumberFormatting() {
    this.addTextChangedListener(object : TextWatcher {
        private var isFormatting: Boolean = false
        private var previousText: String = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (!isFormatting) {
                previousText = s.toString()
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Nothing to do here
        }

        override fun afterTextChanged(s: Editable?) {
            if (isFormatting) return

            isFormatting = true
            val formatted = formatPhoneNumber(s.toString())
            s?.replace(0, s.length, formatted)
            isFormatting = false
        }

        private fun formatPhoneNumber(input: String): String {
            // 숫자만 추출
            val digits = input.replace(Regex("[^\\d]"), "")

            // 포맷 적용
            return when {
                digits.length <= 3 -> digits
                digits.length <= 7 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
                else -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
            }
        }
    })
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
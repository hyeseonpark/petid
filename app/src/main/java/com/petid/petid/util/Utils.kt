package com.petid.petid.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.petid.petid.R
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.view.generate.GeneratePetidMainActivity
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

/**
 * 소수점 제한
 */
class DecimalDigitsInputFilter(private val decimalDigits: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        source ?: return null
        dest ?: return null

        val destText = dest.toString()
        val newText = destText.substring(0, dstart) + source + destText.substring(dend)

        // 소수점 이후로 입력 가능한 자릿수 제한
        if (newText.contains(".")) {
            val parts = newText.split(".")
            if (parts.size > 1 && parts[1].length > decimalDigits) {
                return ""
            }
        }

        return null
    }
}


/**
 * 반려동물 등록 전 dialog
 */
fun petidNullDialog(context: Context) = CustomDialogCommon(
    title = context.getString(R.string.common_dialog_petid_null),
    yesButtonClick = {
        val target = Intent(context, GeneratePetidMainActivity::class.java)
        context.startActivity(target)
    })
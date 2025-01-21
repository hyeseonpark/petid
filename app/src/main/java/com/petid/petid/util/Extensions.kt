package com.petid.petid.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.petid.petid.BuildConfig
import java.io.File
import java.io.FileOutputStream

/**
 * get tag
 */
val Any.TAG: String
    get() = this.javaClass.simpleName

/**
 * 글자 수 관계 없이 말 줄임표 붙이기
 */
fun TextView.setTextWithEllipsis(text: String) {
    this.text = "$text···"
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

/**
 * show dialog dialog
 */
fun View.showLoadingDialog(activity: Activity) {
    ProgressDialogUtil.show(activity)
}

/**
 * hide loading dialog
 */
fun View.hideLoadingDialog() {
    ProgressDialogUtil.cancel()
}


/**
 * Activity: show error message
 */
fun Context.showErrorMessage(text: String) {
    FirebaseCrashlytics.getInstance().log("Error: $text") // Crashlytics 로그 기록

    Log.e(this.TAG, text)
    if (BuildConfig.IS_DEVELOP)
        Toast.makeText(this, "Error Message: $text", Toast.LENGTH_LONG).show()
}

/**
 * Fragment: show error message
 */
fun Fragment.showErrorMessage(text: String) {
    FirebaseCrashlytics.getInstance().log("Error: $text") // Crashlytics 로그 기록

    Log.e(this.TAG, text)
    if (BuildConfig.IS_DEVELOP)
        Toast.makeText(requireContext(), "Error Message: $text", Toast.LENGTH_LONG).show()
}

/**
 * uri to file
 */
fun Uri.toFile(context: Context): File? {
    return try {
        val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(context.contentResolver, this))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }
        bitmap.toFile(context)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 *  Bitmap to File
 */
fun Bitmap.toFile(context: Context): File {
    // 임시 파일 경로
    val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
    // 파일에 Bitmap을 저장
    val fileOutputStream = FileOutputStream(file)
    this.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    fileOutputStream.flush()
    fileOutputStream.close()
    return file
}
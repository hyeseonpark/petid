package com.android.petid.util

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


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
 * dialog
 */
fun View.showLoadingDialog(context: Context) {
    ProgressDialogUtil.show(context)
}

/**
 *
 */
fun View.hideLoadingDialog() {
    ProgressDialogUtil.cancel()
}
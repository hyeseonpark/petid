package com.petid.petid.ui.view.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
fun EditText.flowTextWatcher(): Flow<String> {
    return callbackFlow {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                trySend(editable?.toString() ?: "")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        addTextChangedListener(textWatcher)
        awaitClose {
            removeTextChangedListener(textWatcher)
        }
    }
}
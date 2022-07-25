package com.bootcamp.todoeasy.util

import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.SearchView
import androidx.annotation.RequiresApi


inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

fun AutoCompleteTextView.setOnEnterKeyListener(action: () -> Unit = {}) {
    setOnKeyListener { _, currentKey, keyEvent ->
        if (keyEvent.action == KeyEvent.ACTION_DOWN && currentKey == KeyEvent.KEYCODE_ENTER) {
            action()
            return@setOnKeyListener true
        }

        return@setOnKeyListener false
    }
}

fun EditText.setOnEnterKeyListener(action: () -> Unit = {}) {
    setOnKeyListener { _, currentKey, keyEvent ->
        if (keyEvent.action == KeyEvent.ACTION_DOWN && currentKey == KeyEvent.KEYCODE_ENTER) {
            action()
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }
}

/** HIDE KEYBOARD */
@RequiresApi(Build.VERSION_CODES.R)
fun View.hideKeyboard(view: View){
    windowInsetsController?.hide(WindowInsets.Type.ime())
    view.clearFocus()
}




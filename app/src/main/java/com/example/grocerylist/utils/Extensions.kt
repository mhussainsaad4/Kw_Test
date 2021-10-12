package com.example.grocerylist.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

fun showToast(context: Context, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun manageProgressBar(progressBar: ProgressBar, isVisible: Boolean) {
    if (isVisible) {
        progressBar.visibility = View.VISIBLE
        progressBar.isIndeterminate = true
    } else {
        progressBar?.let {
            it.setVisibility(View.INVISIBLE)
        }
    }
}
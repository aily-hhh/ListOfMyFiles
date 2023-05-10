package com.example.listofmyfiles.ui

import android.view.View

interface FileClickListener {
    fun onClickListener(path: String)
    fun onLongClickListener(path: String, view: View)
}
package com.example.listofmyfiles.ui

interface FileClickListener {
    fun onClickListener(path: String)
    fun onLongClickListener(path: String)
}
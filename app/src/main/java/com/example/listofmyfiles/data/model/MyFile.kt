package com.example.listofmyfiles.data.model

data class MyFile(
    val name: String,
    val size: Long,
    val date: Long,
    val expansion: String,
    val path: String,
    val isEdit: Boolean = false
)

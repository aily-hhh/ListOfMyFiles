package com.example.listofmyfiles.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myFiles")
data class MyFileDB(
    @PrimaryKey()
    val path: String,
    var isEdit: Boolean = false
)

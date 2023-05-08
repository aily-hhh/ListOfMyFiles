package com.example.listofmyfiles.data.dao

import com.example.listofmyfiles.data.model.MyFile
import com.example.listofmyfiles.utils.UiState

interface FileDao {
    suspend fun getFiles(result: (UiState<ArrayList<MyFile>>) -> Unit)
}
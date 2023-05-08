package com.example.listofmyfiles.data.repository

import android.os.Environment
import com.example.listofmyfiles.data.dao.FileDao
import com.example.listofmyfiles.data.model.MyFile
import com.example.listofmyfiles.utils.UiState
import java.io.File
import javax.inject.Inject

class FileRepository @Inject constructor(): FileDao {

    override suspend fun getFiles(result: (UiState<ArrayList<MyFile>>) -> Unit) {
        val directory: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        )
        val file = File(directory.toString())
        val listFiles = file.listFiles()

        if (listFiles != null) {
            val res = arrayListOf<MyFile>()
            for (f in listFiles) {
                val myFile = MyFile(
                    f.nameWithoutExtension,
                    f.totalSpace,
                    f.lastModified(),
                    f.extension,
                    f.absolutePath
                )
                res.add(myFile)
            }
            result.invoke(UiState.Success(res))
        } else {
            result.invoke(UiState.Failure("Файлы не найдены"))
        }
    }
}
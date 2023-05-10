package com.example.listofmyfiles.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.listofmyfiles.data.model.MyFileDB

@Dao
interface FileDatabaseDao {

    @Query("SELECT * FROM myFiles")
    fun getAllFilesFromDB(): LiveData<ArrayList<MyFileDB>>

//    @Query("SELECT * FROM myFiles WHERE path = ?")
//    fun getFileFromDB(): MyFileDB

    @Insert
    suspend fun insertIntoMyFiles(file: MyFileDB)

    @Update
    suspend fun updateIntoMyFiles(isEdit: Boolean)
}
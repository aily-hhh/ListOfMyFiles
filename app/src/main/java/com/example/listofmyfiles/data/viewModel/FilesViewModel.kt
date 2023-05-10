package com.example.listofmyfiles.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listofmyfiles.data.model.MyFile
import com.example.listofmyfiles.data.repository.FileRepository
import com.example.listofmyfiles.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(private val repository: FileRepository): ViewModel() {

    private var _listFiles = MutableLiveData<UiState<ArrayList<MyFile>>>()
    val listFiles: LiveData<UiState<ArrayList<MyFile>>> get() = _listFiles
    fun getAllFiles() {
        _listFiles.value = UiState.Loading
        viewModelScope.launch {
            repository.getFiles {
                _listFiles.value = it
            }
        }
    }

    fun sortAscSize() {
        viewModelScope.launch(Dispatchers.IO) {
            (_listFiles.value as UiState.Success<ArrayList<MyFile>>).data.sortBy {
                it.size
            }
            Log.d("SORT", listFiles.value.toString())
        }
    }

    fun sortDescSize() {
        viewModelScope.launch(Dispatchers.IO) {
            (_listFiles.value as UiState.Success<ArrayList<MyFile>>).data.sortByDescending {
                it.size
            }
            Log.d("SORT", listFiles.value.toString())
        }
    }

    fun sortAscDate() {
        viewModelScope.launch(Dispatchers.IO) {
            (_listFiles.value as UiState.Success<ArrayList<MyFile>>).data.sortBy {
                it.date
            }
            Log.d("SORT", listFiles.value.toString())
        }
    }

    fun sortDescDate() {
        viewModelScope.launch(Dispatchers.IO) {
            (_listFiles.value as UiState.Success<ArrayList<MyFile>>).data.sortByDescending {
                it.date
            }
            Log.d("SORT", listFiles.value.toString())
        }
    }

    fun sortAscExpansion() {
        viewModelScope.launch(Dispatchers.IO) {
            (_listFiles.value as UiState.Success<ArrayList<MyFile>>).data.sortBy {
                it.expansion
            }
            Log.d("SORT", listFiles.value.toString())
        }
    }

    fun sortDescExpansion() {
        viewModelScope.launch(Dispatchers.IO) {
            (_listFiles.value as UiState.Success<ArrayList<MyFile>>).data.sortByDescending {
                it.expansion
            }
            Log.d("SORT", listFiles.value.toString())
        }
    }

    fun sortAllFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            (_listFiles.value as UiState.Success<ArrayList<MyFile>>).data.filter {
                true
            }
            Log.d("SORT", listFiles.value.toString())
        }
    }

    fun sortEditFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            (_listFiles.value as UiState.Success<ArrayList<MyFile>>).data.filter {
                it.name.length < 10
            }
            Log.d("SORT", listFiles.value.toString())
        }
    }
}
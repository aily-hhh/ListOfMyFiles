package com.example.listofmyfiles.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listofmyfiles.data.model.MyFile
import com.example.listofmyfiles.data.repository.FileRepository
import com.example.listofmyfiles.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
}
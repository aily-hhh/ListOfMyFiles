package com.example.listofmyfiles.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.listofmyfiles.R
import com.example.listofmyfiles.data.model.MyFile
import com.example.listofmyfiles.data.viewModel.FilesViewModel
import com.example.listofmyfiles.databinding.ActivityMainBinding
import com.example.listofmyfiles.utils.UiState
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    private var sortTextView: TextView? = null
    private var filesProgressBar: ProgressBar? = null
    private var listFilesRecyclerView: RecyclerView? = null
    private var adapterFiles: FilesAdapter? = null
    private val listForSort: ArrayList<MyFile> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        isStoragePermissionReadGranted()

        filesProgressBar = mBinding.filesProgressBar

        val filesViewModel = ViewModelProvider(this)[FilesViewModel::class.java]
        filesViewModel.getAllFiles()
        filesViewModel.listFiles.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    filesProgressBar?.visibility = View.VISIBLE
                }
                is UiState.Failure -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    filesProgressBar?.visibility = View.GONE
                }
                is UiState.Success -> {
                    adapterFiles?.setDiffer(it.data)
                    listForSort.clear()
                    listForSort.addAll(it.data)
                    filesProgressBar?.visibility = View.GONE
                }
                else -> {
                    Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
                    filesProgressBar?.visibility = View.GONE
                }
            }
        }

        listFilesRecyclerView = mBinding.listFilesRecyclerView
        initAdapter()
        adapterFiles?.setFileClickListener(object: FileClickListener {
            override fun onClickListener(path: String) {
                TODO("Not yet implemented")
            }

            override fun onLongClickListener(path: String) {
                TODO("Not yet implemented")
            }
        })

        sortTextView = mBinding.sortTextView
        sortTextView?.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            val allFilesRB = findViewById<RadioButton>(R.id.allFilesRadioButton)
            val sortRadioGroup = findViewById<RadioGroup>(R.id.sortRadioGroup)

            val saveButton = findViewById<Button>(R.id.saveButton)
            saveButton?.setOnClickListener {
                bottomSheet.dismiss()
            }
            val cancelButton = findViewById<Button>(R.id.cancelButton)
            cancelButton?.setOnClickListener {
                allFilesRB.isChecked = true
                for (i in sortRadioGroup.children) {
                    (i as RadioButton).isChecked = false
                }
                bottomSheet.dismiss()
            }

            bottomSheet.setCancelable(true)
            bottomSheet.setContentView(view)
            bottomSheet.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun changeSort(view: View) {
        if (view is RadioButton) {
            if (view.isChecked) {
                when (view.id) {
                    R.id.ascSizeRadioButton -> {
                        listForSort.sortBy {
                            it.size
                        }
                    }
                    R.id.descSizeRadioButton -> {
                        listForSort.sortByDescending {
                            it.size
                        }
                    }
                    R.id.ascDateRadioButton -> {
                        listForSort.sortBy {
                            it.date
                        }
                    }
                    R.id.descDateRadioButton -> {
                        listForSort.sortByDescending {
                            it.date
                        }
                    }
                    R.id.ascExpansionRadioButton -> {
                        listForSort.sortBy {
                            it.expansion
                        }
                    }
                    R.id.descExpansionRadioButton -> {
                        listForSort.sortByDescending {
                            it.expansion
                        }
                    }
                }
                adapterFiles?.setDiffer(listForSort)
            }
        }
    }

    fun changeTypeListOfFiles(view: View) {
        if (view is RadioButton) {
            if (view.isChecked) {
                when (view.id) {
                    R.id.allFilesRadioButton -> {
                        listForSort.filter {
                            true
                        }
                    }
                    R.id.editFilesRadioButton -> {
                        listForSort.filter {
                            it.isEdit
                        }
                    }
                }
            }
        }
    }

    fun isStoragePermissionReadGranted() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    startActivityIfNeeded(intent, 101)
                } catch (e: java.lang.Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                }
            }
        }
    }

    private fun initAdapter() {
        adapterFiles = FilesAdapter()
        listFilesRecyclerView?.apply {
            adapter = adapterFiles
            layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        }
    }
}
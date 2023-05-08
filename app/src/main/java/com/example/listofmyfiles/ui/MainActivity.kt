package com.example.listofmyfiles.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.listofmyfiles.R
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
    private val STORAGE_REQUEST_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        isStoragePermissionReadGranted()
        isStoragePermissionWriteGranted()
        requestPermission()

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
                        Toast.makeText(this, "ascSizeRadioButton", Toast.LENGTH_SHORT).show()
                    }
                    R.id.descSizeRadioButton -> {
                        Toast.makeText(this, "descSizeRadioButton", Toast.LENGTH_SHORT).show()
                    }
                    R.id.ascDateRadioButton -> {
                        Toast.makeText(this, "ascDateRadioButton", Toast.LENGTH_SHORT).show()
                    }
                    R.id.descDateRadioButton -> {
                        Toast.makeText(this, "descDateRadioButton", Toast.LENGTH_SHORT).show()
                    }
                    R.id.ascExpansionRadioButton -> {
                        Toast.makeText(this, "ascExpansionRadioButton", Toast.LENGTH_SHORT).show()
                    }
                    R.id.descExpansionRadioButton -> {
                        Toast.makeText(this, "descExpansionRadioButton", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun changeTypeListOfFiles(view: View) {
        if (view is RadioButton) {
            if (view.isChecked) {
                when (view.id) {
                    R.id.allFilesRadioButton -> {
                        // all files in list
                    }
                    R.id.editFilesRadioButton -> {
                        // edited files in list
                    }
                }
            }
        }
    }

    private fun haveStoragePermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, STORAGE_REQUEST_PERMISSION)
        }
    }

    fun isStoragePermissionReadGranted(): Boolean {
        val TAG = "Storage Permission"
        return if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
                false
            }
    }

    fun isStoragePermissionWriteGranted(): Boolean {
        val TAG = "Storage Permission"
        return if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
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
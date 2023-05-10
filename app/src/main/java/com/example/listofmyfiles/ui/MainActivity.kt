package com.example.listofmyfiles.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.listofmyfiles.data.model.MyFile
import com.example.listofmyfiles.data.viewModel.FilesViewModel
import com.example.listofmyfiles.databinding.ActivityMainBinding
import com.example.listofmyfiles.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    private var sortTextView: TextView? = null
    private var filesProgressBar: ProgressBar? = null
    private var listFilesRecyclerView: RecyclerView? = null
    private var adapterFiles: FilesAdapter? = null
    private var filesViewModel: FilesViewModel? = null
    private var bottomSheet: BottomSheetSortFragment? = null
    private val resultIntent = Intent("com.example.listofmyfiles.ACTION_RETURN_FILE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        isStoragePermissionReadGranted()

        filesProgressBar = mBinding.filesProgressBar
        bottomSheet = BottomSheetSortFragment()

        filesViewModel = ViewModelProvider(this)[FilesViewModel::class.java]
        filesViewModel?.getAllFiles()
        filesViewModel?.listFiles?.observe(this) {
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
        adapterFiles?.setFileClickListener(object : FileClickListener {
            override fun onClickListener(path: String) {
                Toast.makeText(this@MainActivity, "Click", Toast.LENGTH_SHORT).show()
                val shareIntent = Intent().apply {
                    this.action = Intent.ACTION_SEND
                    this.putExtra(Intent.EXTRA_FROM_STORAGE, File(path))
                    this.type = "file/*"
                }
                startActivity(shareIntent)
            }

            override fun onLongClickListener(path: String, view: View) {
                val requestFile = File(path)
                val fileUri: Uri? = try {
                    FileProvider.getUriForFile(
                        this@MainActivity,
                        "com.example.listofmyfiles.fileprovider",
                        requestFile
                    )
                } catch (e: IllegalArgumentException) {
                    Log.e(
                        "File Selector",
                        "The selected file can't be shared: $requestFile"
                    )
                    null
                }

                if (fileUri != null) {
                    resultIntent.setDataAndType(fileUri, contentResolver.getType(fileUri))
                    setResult(Activity.RESULT_OK, resultIntent)
                } else {
                    resultIntent.setDataAndType(null, "")
                    setResult(RESULT_CANCELED, resultIntent)
                }
            }
        })

        sortTextView = mBinding.sortTextView
        sortTextView?.setOnClickListener {
            bottomSheet?.show(supportFragmentManager, "BottomSheetSort")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isStoragePermissionReadGranted() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 100
            )
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
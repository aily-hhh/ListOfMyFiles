package com.example.listofmyfiles.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        isStoragePermissionReadGranted()

        filesProgressBar = mBinding.filesProgressBar
        bottomSheet = BottomSheetSortFragment()

        filesViewModel = ViewModelProvider(this)[FilesViewModel::class.java]
        initViewModel()

        listFilesRecyclerView = mBinding.listFilesRecyclerView
        initAdapter()
        adapterFiles?.setFileClickListener(object : FileClickListener {
            override fun onClickListener(path: String) {
                openFile(path)
            }

            override fun onLongClickListener(path: String, view: View) {
                shareFile(path)
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

    private fun initViewModel() {
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
    }

    private fun openFile(path: String) {
        val file: File = File(path)
        val contentUri = FileProvider.getUriForFile(
            this@MainActivity,
            applicationContext.packageName + ".provider",
            file
        )
        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        val openFileIntent = Intent(Intent.ACTION_VIEW)
        openFileIntent.setDataAndTypeAndNormalize(contentUri, mime)
        openFileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(openFileIntent)
    }

    private fun shareFile(path: String) {
        val file: File = File(path)
        val contentUri = FileProvider.getUriForFile(
            this@MainActivity,
            applicationContext.packageName + ".provider",
            file
        )
        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        val shareFileIntent = Intent(Intent.ACTION_SEND)
        shareFileIntent.type = mime
        shareFileIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareFileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(shareFileIntent)
    }
}
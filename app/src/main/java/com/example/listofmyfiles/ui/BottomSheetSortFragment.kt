package com.example.listofmyfiles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.listofmyfiles.R
import com.example.listofmyfiles.data.viewModel.FilesViewModel
import com.example.listofmyfiles.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetSortFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDialogBinding? = null
    private val mBinding get() = _binding!!
    private var filesViewModel: FilesViewModel? = null
    private var cancelButton: Button? = null
    private var saveButton: Button? = null
    private var sortRadioGroup: RadioGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filesViewModel = activity?.let { ViewModelProvider(it)[FilesViewModel::class.java] }
        cancelButton = mBinding.cancelButton
        saveButton = mBinding.saveButton

        cancelButton?.setOnClickListener {
            sortRadioGroup?.clearCheck()
            filesViewModel?.sortAllFiles()
        }
        saveButton?.setOnClickListener {
            dismiss()
        }

        sortRadioGroup = mBinding.sortRadioGroup
        sortRadioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.ascSizeRadioButton -> {
                    filesViewModel?.sortAscSize()
                }
                R.id.descSizeRadioButton -> {
                    filesViewModel?.sortDescSize()
                }
                R.id.ascDateRadioButton -> {
                    filesViewModel?.sortAscDate()
                }
                R.id.descDateRadioButton -> {
                    filesViewModel?.sortDescDate()
                }
                R.id.ascExpansionRadioButton -> {
                    filesViewModel?.sortAscExpansion()
                }
                R.id.descExpansionRadioButton -> {
                    filesViewModel?.sortDescExpansion()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
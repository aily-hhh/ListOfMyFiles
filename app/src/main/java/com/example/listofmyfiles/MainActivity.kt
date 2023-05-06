package com.example.listofmyfiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    var sortTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sortTextView = findViewById(R.id.sortTextView)
        sortTextView?.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            val allFilesRB = findViewById<RadioButton>(R.id.allFilesRadioButton)
            val sortRadioGroup = findViewById<RadioGroup>(R.id.sortRadioGroup)

            val saveButton = findViewById<Button>(R.id.saveButton)
            saveButton.setOnClickListener {
                bottomSheet.dismiss()
            }
            val cancelButton = findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
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
}
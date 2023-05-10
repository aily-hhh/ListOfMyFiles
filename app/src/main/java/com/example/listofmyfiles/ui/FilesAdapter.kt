package com.example.listofmyfiles.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.listofmyfiles.R
import com.example.listofmyfiles.data.model.MyFile
import java.util.Date

class FilesAdapter: RecyclerView.Adapter<FilesAdapter.FilesViewHolder>() {

    inner class FilesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        val nameFileTextView: TextView = itemView.findViewById(R.id.nameFileTextView)
        val sizeFileTextView: TextView = itemView.findViewById(R.id.sizeFileTextView)
        val dateFileTextView: TextView = itemView.findViewById(R.id.dateFileTextView)
    }

    private val callback = object: DiffUtil.ItemCallback<MyFile>() {
        override fun areItemsTheSame(oldItem: MyFile, newItem: MyFile): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: MyFile, newItem: MyFile): Boolean {
            return if (oldItem.name != newItem.name) {
                false
            } else if (oldItem.size != newItem.size) {
                false
            } else if (oldItem.date != newItem.date) {
                false
            } else if (oldItem.expansion != newItem.expansion) {
                false
            } else {
                oldItem.path == newItem.path
            }
        }
    }

    private val differ = AsyncListDiffer(this, callback)
    fun setDiffer(list: MutableList<MyFile>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        return FilesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        val currentFile = differ.currentList[position]
        holder.dateFileTextView.text = Date(currentFile.date).toString()
        holder.nameFileTextView.text = currentFile.name
        holder.sizeFileTextView.text = currentFile.size.toString()

        when (currentFile.expansion) {
            "doc", "DOC" -> {
                holder.iconImageView.setImageResource(R.drawable.file_doc)
            }
            "docx", "DOCX" -> {
                holder.iconImageView.setImageResource(R.drawable.file_docx)
            }
            "jpg", "JPG", "jpeg", "JPEG" -> {
                holder.iconImageView.setImageResource(R.drawable.file_jpg)
            }
            "mp3", "MP3" -> {
                holder.iconImageView.setImageResource(R.drawable.file_mp3)
            }
            "mp4", "MP4" -> {
                holder.iconImageView.setImageResource(R.drawable.file_mp4)
            }
            "pdf", "PDF" -> {
                holder.iconImageView.setImageResource(R.drawable.file_pdf)
            }
            "png", "PNG" -> {
                holder.iconImageView.setImageResource(R.drawable.file_png)
            }
            "txt", "TXT" -> {
                holder.iconImageView.setImageResource(R.drawable.file_txt)
            }
            else -> {
                holder.iconImageView.setImageResource(R.drawable.ic_another_file)
            }
        }

        holder.itemView.apply {
            setOnClickListener {
                clickListener!!.onClickListener(currentFile.path)
            }
            setOnLongClickListener {
                clickListener!!.onLongClickListener(currentFile.path, this)
                false
            }
        }
    }

    private var clickListener: FileClickListener? = null
    fun setFileClickListener(clickListener: FileClickListener) {
        this.clickListener = clickListener
    }
}
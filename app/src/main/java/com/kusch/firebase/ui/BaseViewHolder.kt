package com.kusch.firebase.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kusch.firebase.model.ListData

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(dataItem: ListData, position: Int)
}
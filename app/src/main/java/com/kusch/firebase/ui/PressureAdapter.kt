package com.kusch.firebase.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kusch.firebase.R
import com.kusch.firebase.databinding.DateItemBinding
import com.kusch.firebase.databinding.PressureItemBinding
import com.kusch.firebase.model.DatePressure
import com.kusch.firebase.model.ListData
import com.kusch.firebase.model.Pressure
import com.kusch.firebase.model.TYPE_DATE
import java.time.format.DateTimeFormatter

class PressureAdapter() :
    RecyclerView.Adapter<BaseViewHolder>() {
    var dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM")
    var timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private var data: List<ListData> = arrayListOf()

    fun setData(data: List<ListData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            TYPE_DATE ->
                DateViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.date_item, parent, false)
                )
            else ->
                PressureViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.pressure_item, parent, false)
                )
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class DateViewHolder(view: View) : BaseViewHolder(view) {

        private val viewBinding: DateItemBinding by viewBinding()

        override fun bind(item: ListData, position: Int) {
            with(viewBinding) {
                val data = item as DatePressure
                date.text = data.date.format(dateFormatter)
            }
        }
    }

    inner class PressureViewHolder(view: View) : BaseViewHolder(view) {

        private val viewBinding: PressureItemBinding by viewBinding()

        override fun bind(item: ListData, position: Int) {
            with(viewBinding) {
                val data = item as Pressure
                time.text = data.date.format(timeFormatter)
                pressure.text = "${data.pressure_high} / ${data.pressure_low}"
                pulse.text = data.pulse.toString()
                if (data.pressure_high > 130)
                    pressureCard.background =
                        viewBinding.root.context.getDrawable(R.drawable.pressure_background_high)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getType()
    }
}

package com.kusch.firebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

const val TYPE_DATE = 0
const val TYPE_PRESSURE = 1

interface ListData {
    fun getType(): Int
}

@Parcelize
data class Pressure(
    var id: String? = null,
    val date: LocalDateTime,
    val pressure_high: Int,
    val pressure_low: Int,
    val pulse: Int,
) : Parcelable, ListData {
    override fun getType() = TYPE_PRESSURE
}

@Parcelize
data class DatePressure(
    val date: LocalDateTime,
) : Parcelable, ListData {
    override fun getType() = TYPE_DATE
}
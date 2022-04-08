package com.kusch.firebase.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kusch.firebase.model.DatePressure
import com.kusch.firebase.model.FirebaseUtil
import com.kusch.firebase.model.ListData
import com.kusch.firebase.model.Pressure
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PressureViewModel(val firebaseUtil: FirebaseUtil) : ViewModel() {
    val ldPressures: MutableLiveData<List<ListData>> = MutableLiveData()
    var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun requestPressures() {
        viewModelScope.launch {
            val pressures = firebaseUtil.getPressures()
            val list = mutableListOf<ListData>()
            var prevdt = LocalDateTime.parse("1970-03-10 08:15", formatter)
            for (p in pressures.sortedBy { it.date }) {
                if (p.date.dayOfMonth != prevdt.dayOfMonth) {
                    list.add(DatePressure(p.date))
                    prevdt = p.date
                }
                list.add(p)
            }
            viewModelScope.launch {
                ldPressures.value = list
            }
        }
    }

    fun addPressure(it: Pressure) {
        viewModelScope.launch {
            firebaseUtil.savePressure(it, { requestPressures() }, {})
        }
    }
}
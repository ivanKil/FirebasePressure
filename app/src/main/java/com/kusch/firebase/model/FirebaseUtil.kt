package com.kusch.firebase.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FirebaseUtil {
    private val db = Firebase.firestore
    private var curUser: FirebaseUser? = null
    private var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val SEGMENT_PRESSURES = "pressures"
    private val SEGMENT_USERS = "users"
    private val FIELD_DATE = "date"
    private val FIELD_PRESSURE_HIGH = "pressure_high"
    private val FIELD_PRESSURE_LOW = "pressure_low"
    private val PULSE = "pulse"

    suspend fun savePressure(
        pressure: Pressure,
        actOnResult: () -> Unit,
        actOnError: (e: Exception) -> Unit
    ) {
        signIn()
        db.collection(SEGMENT_USERS).document(curUser!!.uid).collection(SEGMENT_PRESSURES)
            .add(pressureToMap(pressure))
            .addOnSuccessListener { actOnResult() }
            .addOnFailureListener { actOnError(it) }
    }

    private fun pressureToMap(pressure: Pressure) = hashMapOf(
        FIELD_DATE to pressure.date.format(formatter),
        FIELD_PRESSURE_HIGH to pressure.pressure_high,
        FIELD_PRESSURE_LOW to pressure.pressure_low,
        PULSE to pressure.pulse
    )


    suspend fun getPressures(): MutableList<Pressure> {
        signIn()
        var list = mutableListOf<Pressure>()
        db.collection(SEGMENT_USERS).document(curUser!!.uid).collection(SEGMENT_PRESSURES)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    list.add(
                        Pressure(
                            null,
                            LocalDateTime.parse(
                                document.data.get(FIELD_DATE).toString(),
                                formatter
                            ),
                            document.data[FIELD_PRESSURE_HIGH].toString().toInt(),
                            document.data[FIELD_PRESSURE_LOW].toString().toInt(),
                            document.data[PULSE].toString().toInt()
                        )
                    )
                }
            }
            .addOnFailureListener { exception ->
            }.await()
        return list
    }

    private suspend fun signIn() {
        if (curUser != null)
            return
        val auth = Firebase.auth
        curUser = auth.currentUser
        if (curUser == null)
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        curUser = auth.currentUser
                    }
                }.await()
    }

}
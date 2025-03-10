package com.experiments.therapaw.data.utils

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.experiments.therapaw.data.model.DelayedTempTimeDataModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.nodes.NODE_TEMPERATURE_10MIN
import com.experiments.therapaw.data.nodes.NODE_TEMPERATURE_1HR
import com.experiments.therapaw.data.nodes.NODE_TEMPERATURE_3MIN
import com.experiments.therapaw.data.nodes.NODE_TEMP_SENSOR
import com.experiments.therapaw.data.nodes.NODE_USERS
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.Transaction.Handler
import com.google.firebase.database.database

class TimedUploadDataUtils(){

    private val auth = Firebase.auth
    private val database = Firebase.database.reference
    private var startTime: Long = System.currentTimeMillis()

    fun delayedUpload() {
        val handler = android.os.Handler(Looper.getMainLooper())

        val delay3min = 18000L
        handler.postDelayed(object : Runnable {
            override fun run() {
                uploadTemperatureData3Min()
                handler.postDelayed(this, delay3min)
            }
        }, delay3min)

        val delay10min = 60000L
        handler.postDelayed(object : Runnable {
            override fun run() {
                uploadTemperatureData10Min()
                handler.postDelayed(this, delay10min)
            }
        }, delay10min)

        val delay1hr = 360000L
        handler.postDelayed(object : Runnable {
            override fun run() {
                uploadTemperatureData1hr()
                handler.postDelayed(this, delay1hr)
            }
        }, delay1hr)
    }

    private fun uploadTemperatureData3Min() {
        val refTemperature = database.child(NODE_TEMP_SENSOR)
            .child("temperature")
        val refTemperature3min = database
            .child(NODE_USERS)
            .child(auth.uid!!)
            .child(NODE_TEMPERATURE_3MIN)

        val timestamp = System.currentTimeMillis()
        val elapsedSeconds = (timestamp - startTime) / 1000
        val timeValue = ((elapsedSeconds % 180) / 18) * 18

        refTemperature.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val temp = snapshot.getValue(Float::class.java) ?: 0f

                val temperatureTimeData = DelayedTempTimeDataModel(
                    time = timeValue,
                    temperature = temp
                )

                refTemperature3min.child(timeValue.toString()).setValue(temperatureTimeData)

                Log.d("uploadData3m", "Time Value: $timeValue | Temp: $temp")
            } else {
                Log.d("uploadData3m", "No temperature data found.")
            }
        }.addOnFailureListener { exception ->
            Log.e("uploadData3m", "Failed to fetch temperature data", exception)
        }
    }

    private fun uploadTemperatureData10Min() {
        val refTemperature = database.child(NODE_TEMP_SENSOR)
            .child("temperature")
        val refTemperature10min = database
            .child(NODE_USERS)
            .child(auth.uid!!)
            .child(NODE_TEMPERATURE_10MIN)

        val timestamp = System.currentTimeMillis()
        val elapsedSeconds = (timestamp - startTime) / 1000
        val timeValue = (elapsedSeconds % 600 / 60) * 60

        refTemperature.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val temp = snapshot.getValue(Float::class.java) ?: 0f

                val temperatureTimeData = DelayedTempTimeDataModel(
                    time = timeValue,
                    temperature = temp
                )

                refTemperature10min.child(timeValue.toString()).setValue(temperatureTimeData)

                Log.d("uploadData10m", "Time Value: $timeValue | Temp: $temp")
            } else {
                Log.d("uploadData10m", "No temperature data found.")
            }
        }.addOnFailureListener { exception ->
            Log.e("uploadData10m", "Failed to fetch temperature data", exception)
        }
    }

    private fun uploadTemperatureData1hr() {
        val refTemperature = database.child(NODE_TEMP_SENSOR)
            .child("temperature")
        val refTemperature1hr = database
            .child(NODE_USERS)
            .child(auth.uid!!)
            .child(NODE_TEMPERATURE_1HR)

        val timestamp = System.currentTimeMillis()
        val elapsedSeconds = (timestamp - startTime) / 1000
        val timeValue = (elapsedSeconds % 3600 / 360) * 360

        refTemperature.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val temp = snapshot.getValue(Float::class.java) ?: 0f

                val temperatureTimeData = DelayedTempTimeDataModel(
                    time = timeValue,
                    temperature = temp
                )

                refTemperature1hr.child(timeValue.toString()).setValue(temperatureTimeData)

                Log.d("uploadData1h", "Time Value: $timeValue | Temp: $temp")
            } else {
                Log.d("uploadData1h", "No temperature data found.")
            }
        }.addOnFailureListener { exception ->
            Log.e("uploadData1h", "Failed to fetch temperature data", exception)
        }
    }
}
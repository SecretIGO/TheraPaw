package com.experiments.therapaw.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.nodes.NODE_TEMP_SENSOR
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class TemperatureViewModel : ViewModel() {
    private val databaseReference = Firebase.database.reference;

    private val _temperatureData = MutableLiveData<TemperatureModel>()
    val temperatureData : LiveData<TemperatureModel> = _temperatureData

    private val temperatureListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val temperature = snapshot.getValue(TemperatureModel::class.java)
            if (temperature != null) {
                _temperatureData.value = temperature
            } else {
                Log.d("Error: ", "Temperature data not received!")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("Error: ", "Listener cancelled: ${error.message}")
        }
    }

    fun fetchTemperatureData() {
        databaseReference.child(NODE_TEMP_SENSOR).addValueEventListener(temperatureListener)
    }
}
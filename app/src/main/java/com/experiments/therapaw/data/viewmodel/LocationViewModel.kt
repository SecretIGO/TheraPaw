package com.experiments.therapaw.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.therapaw.data.model.LocationModel
import com.experiments.therapaw.data.nodes.NODE_GPS_SENSOR
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class LocationViewModel : ViewModel() {
    private val databaseReference = Firebase.database.reference

    private val _locationData = MutableLiveData<LocationModel>()
    val locationData : LiveData<LocationModel> = _locationData

    private val locationListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val location = snapshot.getValue(LocationModel::class.java)
            if (location != null) {
                _locationData.value = location
            } else {
                Log.d("Error: ", "Temperature data not received!")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("Error: ", "Listener cancelled: ${error.message}")
        }
    }

    fun fetchLocationData() {
        databaseReference.child(NODE_GPS_SENSOR).addValueEventListener(locationListener)
    }
}

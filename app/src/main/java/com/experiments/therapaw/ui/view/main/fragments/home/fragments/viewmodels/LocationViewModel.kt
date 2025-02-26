package com.experiments.therapaw.ui.view.main.fragments.home.fragments.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.therapaw.data.model.LocationModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.nodes.NODE_SENSORS
import com.experiments.therapaw.data.nodes.NODE_TEMP_SENSOR
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class LocationViewModel : ViewModel() {
    private val databaseReference = Firebase.database.reference

    private val _locationData = MutableLiveData<LocationModel>()
    val locationData: LiveData<LocationModel> = _locationData

    private val locationListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val location = snapshot.getValue(LocationModel::class.java)
            if (location != null) {
                _locationData.value = location
            } else {
                Log.d("Error", "Location data not received!")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("Error", "Listener cancelled: ${error.message}")
        }
    }

    // ✅ Start listening for real-time updates
    fun startListeningForLocationUpdates() {
        databaseReference.child(NODE_TEMP_SENSOR).addValueEventListener(locationListener)
    }

    // ✅ Remove listener when ViewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        databaseReference.child(NODE_TEMP_SENSOR).removeEventListener(locationListener)
    }

    // ✅ Fetch single location update via callback (alternative method)
    fun fetchLocationData(callback: (LocationModel?) -> Unit) {
        databaseReference.child(NODE_TEMP_SENSOR).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val location = snapshot.getValue(LocationModel::class.java)
                callback(location)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", "Listener cancelled: ${error.message}")
                callback(null)
            }
        })
    }
}

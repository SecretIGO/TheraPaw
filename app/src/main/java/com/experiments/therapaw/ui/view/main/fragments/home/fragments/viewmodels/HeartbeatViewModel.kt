package com.experiments.therapaw.ui.view.main.fragments.home.fragments.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.therapaw.data.model.HeartbeatModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.nodes.NODE_HEART_SENSOR
import com.experiments.therapaw.data.nodes.NODE_TEMP_SENSOR
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class HeartbeatViewModel: ViewModel() {

    private val databaseReference = Firebase.database.reference;

    private val _heartbeatData = MutableLiveData<HeartbeatModel>()
    val heartbeatData : LiveData<HeartbeatModel> = _heartbeatData

    private val heartbeatListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val heartbeat = snapshot.getValue(HeartbeatModel::class.java)
            if (heartbeat != null) {
                _heartbeatData.value = heartbeat
            } else {
                Log.d("Error: ", "Heartbeat data not received!")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("Error: ", "Listener cancelled: ${error.message}")
        }
    }

    fun fetchHeartbeatData() {
        databaseReference.child(NODE_HEART_SENSOR).addValueEventListener(heartbeatListener)
    }
}
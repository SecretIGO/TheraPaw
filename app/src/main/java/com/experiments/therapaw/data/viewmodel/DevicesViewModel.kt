package com.experiments.therapaw.data.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.therapaw.data.model.DeviceDataModel
import com.experiments.therapaw.data.model.HeartbeatModel
import com.experiments.therapaw.data.model.LocationModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.nodes.NODE_DEVICE_DATA
import com.experiments.therapaw.data.nodes.NODE_HEART_DATA
import com.experiments.therapaw.data.nodes.NODE_LOCATION_DATA
import com.experiments.therapaw.data.nodes.NODE_TEMPERATURE_DATA
import com.experiments.therapaw.data.nodes.NODE_USERS
import com.experiments.therapaw.data.states.AuthenticationStates
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import java.util.Date

class DevicesViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database.reference

    private var authenticationStates = MutableLiveData<AuthenticationStates>()

    fun fetchDeviceDataTracking(onDeviceDataFetched: (DeviceDataModel) -> Unit) {
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            return
        }

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child(NODE_DEVICE_DATA)

        deviceRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    Log.d("DeviceTrackingStatuses", snapshot?.value.toString())

                    val isActive = snapshot.child("isActive").getValue(Boolean::class.java)
                    val temperatureActive = snapshot.child("temperatureData")
                        .child("isActive").getValue(Boolean::class.java)
                    val temperatureModel = TemperatureModel(
                        isActive = temperatureActive
                    )
                    val locationActive = snapshot.child("locationData")
                        .child("isActive").getValue(Boolean::class.java)
                    val locationModel = LocationModel(
                        isActive = locationActive
                    )
                    val heartActive = snapshot.child("heartData")
                        .child("isActive").getValue(Boolean::class.java)
                    val heartbeatModel = HeartbeatModel(
                        isActive = heartActive
                    )

                    val dateTimestamp = snapshot.child("date").getValue(Long::class.java)
                    var date: Date? = null;

                    if(dateTimestamp != null){
                        date = Date(dateTimestamp)
                    }

                    val deviceData = DeviceDataModel(
                        date,
                        isActive,
                        temperatureModel,
                        locationModel,
                        heartbeatModel
                    )

                    onDeviceDataFetched(deviceData)

                } else {
                    Log.d("Error", "Failed to fetch temperature data: ${task.exception?.message}")
                }
            }
    }

    fun toggleDeviceDataTracking(isActive: Boolean){
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            return
        }

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child(NODE_DEVICE_DATA)
            .child("isActive")

        deviceRef.setValue(isActive)
    }

    fun toggleTemperatureTracking(isActive: Boolean){
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            return
        }

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child(NODE_TEMPERATURE_DATA)
            .child("isActive")

        deviceRef.setValue(isActive)
    }

    fun toggleLocationTracking(isActive: Boolean){
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            return
        }

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child(NODE_LOCATION_DATA)
            .child("isActive")

        deviceRef.setValue(isActive)
    }

    fun toggleHeartbeatTracking(isActive: Boolean){
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            return
        }

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child(NODE_HEART_DATA)
            .child("isActive")

        deviceRef.setValue(isActive)
    }
}
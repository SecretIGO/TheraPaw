package com.experiments.therapaw.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.grouplink.data.states.StorageStates
import com.experiments.therapaw.data.model.DelayedTempTimeDataModel
import com.experiments.therapaw.data.model.DeviceDataModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.nodes.NODE_USERS
import com.experiments.therapaw.data.states.AuthenticationStates
import com.experiments.therapaw.data.utils.saveImageToInternalStorage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class UserViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database.reference

    private var authenticationStates = MutableLiveData<AuthenticationStates>()
    private var storageStates = MutableLiveData<StorageStates>()

    private val _temperature3minLiveData = MutableLiveData<List<DelayedTempTimeDataModel>>()
    val temperature3minLiveData: LiveData<List<DelayedTempTimeDataModel>> get() = _temperature3minLiveData

    private val _temperature10minLiveData = MutableLiveData<List<DelayedTempTimeDataModel>>()
    val temperature10minLiveData: LiveData<List<DelayedTempTimeDataModel>> get() = _temperature10minLiveData

    private val _temperature1hrLiveData = MutableLiveData<List<DelayedTempTimeDataModel>>()
    val temperature1hrLiveData: LiveData<List<DelayedTempTimeDataModel>> get() = _temperature1hrLiveData

    fun addUserData(
        userInformation: UserModel,
        img: ByteArray?,
        context: Context,
        onUserDataAdded: () -> Unit
    ) {
        val filePath = if (img != null) {
            saveImageToInternalStorage(context, img, "${userInformation.uid}.jpg")
        } else {
            null
        }

        if (filePath != null) {
            userInformation.profilePicture = filePath

            val userRef = database.child(NODE_USERS)
                .child(auth.uid!!)

            userRef.setValue(userInformation)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        authenticationStates.value = AuthenticationStates.UserCreated
                        onUserDataAdded.invoke()
                    } else {
                        authenticationStates.value = AuthenticationStates.Error
                    }
                }
        } else {
            storageStates.value = StorageStates.StorageFailed("Failed to save image locally")
        }
    }

    fun addDeviceDataToDailyRecord(
        deviceDataModel: DeviceDataModel,
        onDeviceDataAdded: () -> Unit
    ) {
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            return
        }

        val userRef = database.child(NODE_USERS)
            .child(userUid)
            .child("petDailyRecord")

        userRef.setValue(deviceDataModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onDeviceDataAdded.invoke()
                } else {
                    authenticationStates.value = AuthenticationStates.Error
                    Log.d("Error", "Failed to update petDailyRecord: ${task.exception?.message}")
                }
            }
    }

    fun updateTemperatureData(
        temperatureData: TemperatureModel
    ) {
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            return
        }

        temperatureData.isActive = null
        temperatureData.temperature = null

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child("petDailyRecord")
            .child("temperatureData")

        deviceRef.setValue(temperatureData)
    }

    fun fetchDeviceData() {
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            return
        }

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child("deviceData")

        deviceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val deviceData = snapshot.getValue(DeviceDataModel::class.java)
                val temp3minList = deviceData?.temperatureData?.temp3minData?.values?.toList()
                val temp10minList = deviceData?.temperatureData?.temp10minData?.values?.toList()
                val temp1hrList = deviceData?.temperatureData?.temp1hrData?.values?.toList()

                if (temp3minList != null) {
                    _temperature3minLiveData.value = temp3minList
                }
                if (temp10minList != null) {
                    _temperature10minLiveData.value = temp10minList
                }
                if (temp1hrList != null) {
                    _temperature1hrLiveData.value = temp1hrList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", "Failed to fetch temperature data: ${error.message}")
            }
        })
    }

    fun fetchDailyRecord(onDailyRecordFetched: (DeviceDataModel?) -> Unit) {
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            onDailyRecordFetched(null)
            return
        }

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child("petDailyRecord")

        deviceRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    val deviceData = snapshot.getValue(DeviceDataModel::class.java)
                    Log.d("daily", snapshot.value.toString())
                    onDailyRecordFetched(deviceData)
                } else {
                    Log.d("Error", "Failed to fetch temperature data: ${task.exception?.message}")
                    onDailyRecordFetched(null)
                }
            }
    }
}

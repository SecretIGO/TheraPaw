package com.experiments.therapaw.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.grouplink.data.states.StorageStates
import com.experiments.therapaw.data.model.DeviceDataModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.nodes.NODE_USERS
import com.experiments.therapaw.data.states.AuthenticationStates
import com.experiments.therapaw.data.utils.saveImageToInternalStorage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class UserViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database.reference

    private var authenticationStates = MutableLiveData<AuthenticationStates>()
    private var storageStates = MutableLiveData<StorageStates>()

    private val _deviceDataModel = MutableLiveData<DeviceDataModel>()
    val deviceDataModel: LiveData<DeviceDataModel> get() = _deviceDataModel

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

        val userRef = database.child(NODE_USERS).child(userUid)

        userRef.child("petDailyRecord").setValue(deviceDataModel)
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

        val deviceRef = database.child(NODE_USERS)
            .child(userUid)
            .child("petDailyRecord")
            .child("temperature")

        deviceRef.setValue(temperatureData)
    }

    fun fetchDeviceData(onTemperatureFetched: (DeviceDataModel?) -> Unit) {
        val userUid = auth.uid
        if (userUid == null) {
            authenticationStates.value = AuthenticationStates.Error
            onTemperatureFetched(null)
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
                    onTemperatureFetched(deviceData)
                } else {
                    Log.d("Error", "Failed to fetch temperature data: ${task.exception?.message}")
                    onTemperatureFetched(null)
                }
            }

    }
}

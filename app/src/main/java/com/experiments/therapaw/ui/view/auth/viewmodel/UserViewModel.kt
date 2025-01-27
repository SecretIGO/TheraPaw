package com.experiments.therapaw.ui.view.auth.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.grouplink.data.states.StorageStates
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

    fun addUserData(
        userInformation: UserModel,
        img: ByteArray,
        context: Context,
        onUserDataAdded: () -> Unit
    ) {
        val filePath = saveImageToInternalStorage(context, img, "${userInformation.uid}.jpg")
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
}

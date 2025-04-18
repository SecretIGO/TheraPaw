package com.experiments.therapaw.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.experiments.therapaw.data.model.AuthModel
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.states.AuthenticationStates
import com.experiments.therapaw.data.viewmodel.AuthViewmodel
import com.experiments.therapaw.ui.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

val auth = FirebaseAuth.getInstance()
val database = FirebaseDatabase.getInstance().reference

fun createUserModel(userUid: String, username: String, email: String, filePath: String): UserModel {
    return UserModel(
        uid = userUid,
        username = username,
        email = email,
        profilePicture = filePath,
        listPetId = null,
        listOtherProfiles = null
    )
}

fun createAuthModel(email: String, password: String): AuthModel {
    return AuthModel(
        email = email,
        password = password
    )
}

fun fetchUserData(context: Context, onUserDataFetched: (UserModel) -> Unit) {
    val authId = auth.uid ?: return

    database.child("users").child(authId).get().addOnSuccessListener { snapshot ->
        val userInfo = snapshot.getValue(UserModel::class.java)

        if (userInfo != null) {
            onUserDataFetched(userInfo)
        } else {
            Toast.makeText(context, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
    }
}

suspend fun signup(
    context: Context,
    bitmap: Bitmap?,
    authValues: AuthModel,
    userValues: UserModel,
    auth: AuthViewmodel,
    saveImageToInternalStorage: (Context, ByteArray, String) -> String?,
    addUserData: suspend (UserModel, ByteArray?, Context, () -> Unit) -> Unit
) {
    try {
        auth.setAuthState(AuthenticationStates.Loading)

        val result = auth.signUp(authValues.email, authValues.password)
        if (result == AuthenticationStates.SignedUp) {
            handleDataPathing(context, bitmap, userValues, auth, saveImageToInternalStorage, addUserData)
        } else {
            showError(auth, context, "Signup failed!")
        }
    } catch (e: Exception) {
        showError(auth, context, "An error occurred: ${e.message}")
    }
}

suspend fun signin(
    context: Context,
    authValues: AuthModel,
    auth: AuthViewmodel
){
    val result = auth.signIn(authValues.email, authValues.password)
    if(result == AuthenticationStates.SignedIn){
        MainActivity.launch(context)
    } else {
        showError(auth, context, "Signin failed!")
    }

}

private suspend fun handleDataPathing(
    context: Context,
    bitmap: Bitmap?,
    userValues: UserModel,
    auth: AuthViewmodel,
    saveImageToInternalStorage: (Context, ByteArray, String) -> String?,
    addUserData: suspend (UserModel, ByteArray?, Context, () -> Unit) -> Unit
) {
    val imageBytes = bitmap?.let { compressBitmapToBytes(it) }
    val filePath = if (imageBytes != null) {
        saveImageToInternalStorage(context, imageBytes, "${auth.getCurrentUserUid()}.jpg")
    } else {
        null
    }

    val userInfo = createUserModel(auth.getCurrentUserUid()!!, userValues.username, userValues.email, filePath ?: "")
    saveUserData(userInfo, imageBytes, auth, context, addUserData)
}

private fun compressBitmapToBytes(bitmap: Bitmap): ByteArray {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    return baos.toByteArray()
}

private suspend fun saveUserData(
    userInfo: UserModel,
    imageBytes: ByteArray?,
    auth: AuthViewmodel,
    context: Context,
    addUserData: suspend (UserModel, ByteArray?, Context, () -> Unit) -> Unit
) {
    addUserData(userInfo, imageBytes, context) {
        auth.setAuthState(AuthenticationStates.UserCreated)
        if (auth.getAuthStates().value == AuthenticationStates.UserCreated) {
            MainActivity.launch(context)
        }
    }
}

private fun showError(auth: AuthViewmodel, context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    auth.setAuthState(AuthenticationStates.Error)
}

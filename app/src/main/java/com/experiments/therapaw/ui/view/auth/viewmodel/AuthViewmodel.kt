package com.experiments.therapaw.ui.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.nodes.NODE_USERS
import com.experiments.therapaw.data.states.AuthenticationStates
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

class AuthViewmodel: ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database.reference

    private var authenticationStates = MutableLiveData<AuthenticationStates>()

    fun getAuthStates() : LiveData<AuthenticationStates> = authenticationStates

    fun setAuthStates(newState: AuthenticationStates) {
        authenticationStates.value = newState
    }

    fun isSignedIn() {
        authenticationStates.value = AuthenticationStates.IsSignedIn(auth.currentUser != null)
    }

    suspend fun signUp(email: String, password: String): AuthenticationStates {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthenticationStates.SignedUp
        } catch (e: Exception) {
            AuthenticationStates.Error
        }
    }

    suspend fun signIn(email : String, password : String): AuthenticationStates {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthenticationStates.SignedIn
        } catch (e: Exception) {
            AuthenticationStates.Error
        }
    }

    fun logout(){
        auth.signOut()
        authenticationStates.value = AuthenticationStates.LogOut
    }

    fun getUserProfile() {
        val objectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue<UserModel>()
                authenticationStates.value = AuthenticationStates.Default(users)
            }

            override fun onCancelled(error: DatabaseError) {
                authenticationStates.value = AuthenticationStates.Error
            }
        }

        database.child(NODE_USERS + "/" + auth.currentUser?.uid).addValueEventListener(objectListener)
    }

    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }



}
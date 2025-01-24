package com.experiments.therapaw.data.states

import com.experiments.therapaw.data.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

sealed class AuthenticationStates {

    data class Default(val user : UserModel?) : AuthenticationStates()
    data class IsSignedIn(val isSignedIn : Boolean) : AuthenticationStates()
    data object SignedUp : AuthenticationStates()
    data object SignedIn : AuthenticationStates()

    data object Loading : AuthenticationStates()
    data object ProfileUpdated : AuthenticationStates()
    data object EmailUpdated : AuthenticationStates()
    data object PasswordUpdated : AuthenticationStates()

    data object VerificationEmailSent : AuthenticationStates()
    data object PasswordResetEmailSent : AuthenticationStates()

    data object LogOut : AuthenticationStates()
    data object UserCreated : AuthenticationStates()
    data object UserDeleted : AuthenticationStates()

    data object Error : AuthenticationStates()
}
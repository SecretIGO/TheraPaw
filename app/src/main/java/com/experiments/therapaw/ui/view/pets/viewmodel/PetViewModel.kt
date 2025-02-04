package com.experiments.therapaw.ui.view.pets.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiments.grouplink.data.states.PetStates
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class PetViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database.reference

    private var petState = MutableLiveData<PetStates>()

    fun getPetStates() : LiveData<PetStates> = petState

    fun setPetState(state : PetStates) {
        petState.value = state
    }


}
package com.experiments.grouplink.data.states

import com.experiments.therapaw.data.model.PetModel
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.states.AuthenticationStates

sealed class PetStates {

    data class Default(val pet : PetModel?) : PetStates()
    data object CurrentPet : PetStates()

    data object AddSuccessful : PetStates()
    data class AddFailed(val message: String?) : PetStates()

}

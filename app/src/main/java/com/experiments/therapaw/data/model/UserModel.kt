package com.experiments.therapaw.data.model

data class UserModel (
    val uid : String = "",
    val username : String = "",
    val email : String = "",
    var profilePicture : String? = null,
    val listPetId : List<String>? = null,
    val listOtherProfiles : List<String>? = null
)
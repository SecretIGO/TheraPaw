package com.experiments.therapaw.data.model

data class PetModel (
    val uid : String = "",
    val ownerUID : String = "",
    val petName : String = "",
    val size : String? = "",
    val breed : String? = "",
    val weight : Float? = 0.0F,
    val height : Float? = 0.0F,
    var profilePicture : String? = ""
)
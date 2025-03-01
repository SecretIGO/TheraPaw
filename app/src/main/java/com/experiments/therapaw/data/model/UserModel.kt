package com.experiments.therapaw.data.model

data class UserModel (
    val uid : String = "",
    val username : String = "",
    val email : String = "",
    val petDailyRecord : DeviceDataModel? = null,
    val petAllRecords : ArrayList<DeviceDataModel>? = null,
    var profilePicture : String? = "",
    val listPetId : List<String>? = null,
    val listOtherProfiles : List<String>? = null
)
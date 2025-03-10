package com.experiments.therapaw.data.model

data class UserModel (
    val uid : String = "",
    val username : String = "",
    val email : String = "",
    var profilePicture : String? = "",
    val deviceData : DeviceDataModel? = null,
    val petDailyRecord : DeviceDataModel? = null,
    val petAllRecords : ArrayList<DeviceDataModel>? = null,
    val listPetId : List<String>? = null,
    val listOtherProfiles : List<String>? = null
)
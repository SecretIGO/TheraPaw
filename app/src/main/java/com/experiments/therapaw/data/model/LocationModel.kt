package com.experiments.therapaw.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class LocationModel (
    var isActive : Boolean? = false,
    val latitude : Double = 0.0,
    val longitude : Double = 0.0
) : Parcelable
package com.experiments.therapaw.data.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
class TemperatureModel (
    val temperature : Double? = 0.0,
    val maxTemperature : Double? = 0.0,
    val minTemperature : Double? = 0.0
) : Parcelable
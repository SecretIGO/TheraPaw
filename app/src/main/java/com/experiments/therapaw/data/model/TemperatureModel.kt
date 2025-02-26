package com.experiments.therapaw.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TemperatureModel (
    val temperature : Double = 0.0,
    var humidity : Double = 0.0
) : Parcelable
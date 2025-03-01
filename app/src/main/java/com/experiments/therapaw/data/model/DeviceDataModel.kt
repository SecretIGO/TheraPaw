package com.experiments.therapaw.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class DeviceDataModel (
    val temperatureData: TemperatureModel? = null,
    val locationData: LocationModel? = null,
    val heartData: HeartbeatModel? = null
) : Parcelable
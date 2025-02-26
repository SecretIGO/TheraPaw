package com.experiments.therapaw.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class HeartbeatModel (
    val bpm : Double = 0.0,
    val average_bpm : Int = 0
) : Parcelable
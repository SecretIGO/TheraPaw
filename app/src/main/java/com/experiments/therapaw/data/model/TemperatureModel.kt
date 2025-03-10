package com.experiments.therapaw.data.model

data class TemperatureModel(
    var isActive: Boolean? = false,
    var temperature: Double? = 0.0,
    val maxTemperature: Double = 0.0,
    val minTemperature: Double = 0.0,
    val temp3minData: Map<String, DelayedTempTimeDataModel>? = emptyMap(),
    val temp10minData: Map<String, DelayedTempTimeDataModel>? = emptyMap(),
    val temp1hrData: Map<String, DelayedTempTimeDataModel>? = emptyMap(),
)
package com.experiments.therapaw.data.model

import java.util.Date

class DeviceDataModel (
    var date: Date? = null,
    var isActive: Boolean? = false,
    var temperatureData: TemperatureModel? = TemperatureModel(),
    var locationData: LocationModel? = LocationModel(),
    var heartData: HeartbeatModel? = HeartbeatModel()
)
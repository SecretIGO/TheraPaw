package com.experiments.therapaw.data.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.experiments.therapaw.data.model.HeartbeatModel
import com.experiments.therapaw.data.model.TemperatureModel

class CombinedViewModel(
    heartbeatViewModel: HeartbeatViewModel,
    temperatureViewModel: TemperatureViewModel
) : ViewModel() {

    val combinedLiveData = MediatorLiveData<Pair<HeartbeatModel?, TemperatureModel?>>()

    init {
        combinedLiveData.addSource(heartbeatViewModel.heartbeatData) { heartbeat ->
            val temperature = temperatureViewModel.temperatureData.value
            combinedLiveData.value = Pair(heartbeat, temperature)
        }

        combinedLiveData.addSource(temperatureViewModel.temperatureData) { temperature ->
            val heartbeat = heartbeatViewModel.heartbeatData.value
            combinedLiveData.value = Pair(heartbeat, temperature)
        }
    }
}
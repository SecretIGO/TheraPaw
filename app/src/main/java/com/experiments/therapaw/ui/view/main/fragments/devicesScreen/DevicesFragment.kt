package com.experiments.therapaw.ui.view.main.fragments.devicesScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.experiments.therapaw.R
import com.experiments.therapaw.data.viewmodel.DevicesViewModel
import com.experiments.therapaw.data.viewmodel.HeartbeatViewModel
import com.experiments.therapaw.databinding.FragmentDevicesBinding
import com.experiments.therapaw.databinding.FragmentHeartbeatBinding

class DevicesFragment : Fragment() {

    private lateinit var binding : FragmentDevicesBinding
    private lateinit var devicesViewModel : DevicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDevicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        devicesViewModel = DevicesViewModel()

        bind()
    }

    private fun bind() {
        with(binding){
            devicesViewModel.fetchDeviceDataTracking { deviceData ->
                switchPetMonitoring.isChecked = deviceData.isActive ?: false
                switchTemperature.isChecked = deviceData.temperatureData?.isActive ?: false
                switchLocation.isChecked = deviceData.locationData?.isActive ?: false
                switchHeartbeat.isChecked = deviceData.heartData?.isActive ?: false
            }

            switchPetMonitoring.setOnClickListener{
                devicesViewModel.toggleDeviceDataTracking(switchPetMonitoring.isChecked)
            }

            switchTemperature.setOnClickListener{
                devicesViewModel.toggleTemperatureTracking(switchTemperature.isChecked)
            }

            switchLocation.setOnClickListener{
                devicesViewModel.toggleLocationTracking(switchLocation.isChecked)
            }

            switchHeartbeat.setOnClickListener{
                devicesViewModel.toggleHeartbeatTracking(switchHeartbeat.isChecked)
            }
        }
    }
}
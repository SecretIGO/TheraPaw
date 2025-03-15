package com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import com.experiments.therapaw.R
import com.experiments.therapaw.data.model.HeartbeatModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.viewmodel.CombinedViewModel
import com.experiments.therapaw.data.viewmodel.DevicesViewModel
import com.experiments.therapaw.databinding.FragmentHeartbeatBinding
import com.experiments.therapaw.data.viewmodel.HeartbeatViewModel
import com.experiments.therapaw.data.viewmodel.TemperatureViewModel

class HeartbeatFragment : Fragment() {

    private lateinit var binding: FragmentHeartbeatBinding
    private lateinit var heartbeatViewModel: HeartbeatViewModel
    private lateinit var temperatureViewModel: TemperatureViewModel
    private lateinit var devicesViewModel: DevicesViewModel
    private lateinit var combinedViewModel: CombinedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeartbeatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        heartbeatViewModel = HeartbeatViewModel()
        temperatureViewModel = TemperatureViewModel()
        devicesViewModel = DevicesViewModel()

        combinedViewModel = CombinedViewModel(heartbeatViewModel, temperatureViewModel)

        heartbeatViewModel.fetchHeartbeatData()

        bind()
    }

    fun bind() {
        with(binding) {

            devicesViewModel.fetchDeviceDataTracking { deviceStatus ->

                switchDevice.isChecked = deviceStatus.heartData?.isActive ?: false

                if (
                    (deviceStatus.isActive != null && deviceStatus.isActive == false) ||
                    (deviceStatus.heartData != null && deviceStatus.heartData?.isActive == false)
                ) {
                    val backgroundColor = ColorStateList.valueOf(
                        getColor(
                            requireContext(),
                            R.color.backgroundDisabled
                        )
                    )
                    val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textDisabled))

                    with(binding){
                        txtWarning.text = getString(R.string.device_is_currently_turned_off)
                        layoutWarning.visibility = View.VISIBLE
                        cardBackgroundBPM.backgroundTintList = backgroundColor
                        valBpm.setTextColor(textColor)
                        imgHeartbeat.imageTintList = textColor
                        lblBpm.setTextColor(textColor)
                        textDescription.text = getString(R.string.pet_no_connection)
                    }
                }

                if(deviceStatus.temperatureData?.isActive == true){
                    temperatureViewModel.fetchTemperatureData()
                }

                switchDevice.setOnClickListener {

                    devicesViewModel.toggleHeartbeatTracking(switchDevice.isChecked)

                    if(!switchDevice.isChecked) {

                        val backgroundColor = ColorStateList.valueOf(
                            getColor(
                                requireContext(),
                                R.color.backgroundDisabled
                            )
                        )
                        val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textDisabled))

                        with(binding){
                            txtWarning.text = getString(R.string.device_is_currently_turned_off)
                            layoutWarning.visibility = View.VISIBLE
                            cardBackgroundBPM.backgroundTintList = backgroundColor
                            valBpm.setTextColor(textColor)
                            imgHeartbeat.imageTintList = textColor
                            lblBpm.setTextColor(textColor)
                            textDescription.text = getString(R.string.pet_no_connection)
                        }
                    } else {
                        val backgroundColor = ColorStateList.valueOf(
                            getColor(
                                requireContext(),
                                R.color.backgroundCold
                            )
                        )
                        val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textCold))

                        with(binding){
                            txtWarning.text = getString(R.string.heart_warn_not_enough_pressure)
                            layoutWarning.visibility = View.GONE
                            cardBackgroundBPM.backgroundTintList = backgroundColor
                            valBpm.setTextColor(textColor)
                            imgHeartbeat.imageTintList = textColor
                            lblBpm.setTextColor(textColor)
                            textDescription.text = getString(R.string.pet_no_connection)
                        }
                    }
                }
            }
        }

        observeHeartbeatData()
    }

    private fun observeHeartbeatData() {

        combinedViewModel.combinedLiveData.observe(viewLifecycleOwner) { (heartbeatData, temperatureData) ->

            if (heartbeatData != null) {

                devicesViewModel.fetchDeviceDataTracking { deviceStatus ->

                    if (deviceStatus.isActive == true && deviceStatus.heartData?.isActive == true) {

                        manageUIBasedOnHeartData(heartbeatData)

                        if (temperatureData != null) {

                            manageDescriptionBasedOnHeartDataAndTemperatureData(heartbeatData, temperatureData)
                        }
                    } else {

                        val backgroundColor = ColorStateList.valueOf(
                            getColor(
                                requireContext(),
                                R.color.backgroundDisabled
                            )
                        )
                        val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textDisabled))

                        with(binding){
                            txtWarning.text = getString(R.string.device_is_currently_turned_off)
                            layoutWarning.visibility = View.VISIBLE
                            cardBackgroundBPM.backgroundTintList = backgroundColor
                            valBpm.setTextColor(textColor)
                            imgHeartbeat.imageTintList = textColor
                            lblBpm.setTextColor(textColor)
                            textDescription.text = getString(R.string.pet_no_connection)
                        }
                    }
                }
            }
        }
    }

    private fun manageUIBasedOnHeartData(heartData: HeartbeatModel) {
        with(binding) {

            val backgroundColor: ColorStateList
            val textColor: ColorStateList

            if (heartData.ir_value > 115420) {

                layoutWarning.visibility = View.GONE
                valBpm.text = heartData.average_bpm.toString()

                if (heartData.average_bpm < 90) {
                    textDescription.text = getString(R.string.desc_heart_rate_low)

                    backgroundColor = ColorStateList.valueOf(
                        getColor(
                            requireContext(),
                            R.color.backgroundCold
                        )
                    )
                    textColor =
                        ColorStateList.valueOf(getColor(requireContext(), R.color.textCold))
                    imgDescription.visibility = View.VISIBLE
                } else if (heartData.average_bpm > 140) {
                    textDescription.text = getString(R.string.desc_heart_rate_high)
                    backgroundColor = ColorStateList.valueOf(
                        getColor(
                            requireContext(),
                            R.color.backgroundWarm
                        )
                    )
                    textColor =
                        ColorStateList.valueOf(getColor(requireContext(), R.color.textWarm))
                    imgDescription.visibility = View.VISIBLE
                } else {
                    textDescription.text = getString(R.string.desc_heart_rate_normal)
                    backgroundColor = ColorStateList.valueOf(
                        getColor(
                            requireContext(),
                            R.color.backgroundRegular
                        )
                    )
                    textColor = ColorStateList.valueOf(
                        getColor(
                            requireContext(),
                            R.color.textRegular
                        )
                    )
                    imgDescription.visibility = View.GONE
                }
            } else {
                valBpm.text = "0"

                backgroundColor = ColorStateList.valueOf(
                    getColor(
                        requireContext(),
                        R.color.backgroundRegular
                    )
                )
                textColor =
                    ColorStateList.valueOf(getColor(requireContext(), R.color.textRegular))
                layoutWarning.visibility = View.VISIBLE
                txtWarning.text = getString(R.string.heart_warn_not_enough_pressure)
                textDescription.text = getString(R.string.heart_not_attached)
            }

            cardBackgroundBPM.backgroundTintList = backgroundColor
            imgHeartbeat.imageTintList = textColor
            lblBpm.setTextColor(textColor)
            valBpm.setTextColor(textColor)
        }
    }

    private fun manageDescriptionBasedOnHeartDataAndTemperatureData(
        heartData: HeartbeatModel,
        tempData: TemperatureModel
    ) {
        with(binding) {

            if (heartData.ir_value > 115420) {

                layoutWarning.visibility = View.GONE
                valBpm.text = heartData.average_bpm.toString()

                val temperature = tempData.temperature ?: 0.0

                Log.d("combined", "temperature: " + temperature)

                when {
                    heartData.average_bpm < 90 && temperature < 7.82 -> {
                        textDescription.text = getString(R.string.pet_heart_low_temp_low)
                        imgDescription.visibility = View.VISIBLE
                    }
                    heartData.average_bpm > 140 && temperature > 29.4 -> {
                        textDescription.text = getString(R.string.pet_heart_high_temp_high)
                        imgDescription.visibility = View.VISIBLE
                    }
                    heartData.average_bpm < 90 && temperature > 29.4 -> {
                        textDescription.text = getString(R.string.pet_heart_low_temp_high)
                        imgDescription.visibility = View.VISIBLE
                    }
                    heartData.average_bpm > 140 && temperature < 29.4 -> {
                        textDescription.text = getString(R.string.pet_heart_high_temp_low)
                        imgDescription.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
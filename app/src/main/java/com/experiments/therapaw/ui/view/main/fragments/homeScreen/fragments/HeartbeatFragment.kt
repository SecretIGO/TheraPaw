package com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import com.experiments.therapaw.R
import com.experiments.therapaw.data.model.HeartbeatModel
import com.experiments.therapaw.data.viewmodel.DevicesViewModel
import com.experiments.therapaw.databinding.FragmentHeartbeatBinding
import com.experiments.therapaw.data.viewmodel.HeartbeatViewModel

class HeartbeatFragment : Fragment() {

    private lateinit var binding: FragmentHeartbeatBinding
    private lateinit var heartbeatViewModel: HeartbeatViewModel
    private lateinit var devicesViewModel: DevicesViewModel

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
        devicesViewModel = DevicesViewModel()

        bind()

        observeHeartbeatData()
        heartbeatViewModel.fetchHeartbeatData()
    }

    fun bind() {
        with(binding) {
            devicesViewModel.fetchDeviceDataTracking { deviceStatus ->
                switchDevice.isChecked = deviceStatus.heartData?.isActive ?: false

                switchDevice.setOnClickListener {
                    devicesViewModel.toggleHeartbeatTracking(switchDevice.isChecked)

                    if(!    switchDevice.isChecked) {
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
    }

    private fun observeHeartbeatData() {
        heartbeatViewModel.heartbeatData.observe(viewLifecycleOwner) { heartbeatData ->

            devicesViewModel.fetchDeviceDataTracking { deviceStatus ->

                if (deviceStatus.heartData?.isActive == true) {
                    if (heartbeatData != null) {
                        manageUIBasedOnHeartData(heartbeatData)
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
}
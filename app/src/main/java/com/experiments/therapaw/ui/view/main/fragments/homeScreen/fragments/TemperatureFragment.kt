package com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.experiments.therapaw.R
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.viewmodel.TemperatureViewModel
import com.experiments.therapaw.data.viewmodel.UserViewModel
import com.experiments.therapaw.databinding.FragmentTemperatureBinding

class TemperatureFragment : Fragment() {

    private lateinit var binding : FragmentTemperatureBinding
    private lateinit var temperatureViewModel: TemperatureViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemperatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        temperatureViewModel = TemperatureViewModel()
        userViewModel = UserViewModel()

        observeTemperatureData()
        temperatureViewModel.fetchTemperatureData()

        bind()
    }

    private fun bind(){
        userViewModel.fetchDeviceData { deviceData ->
            with(binding){
                if (deviceData?.temperatureData != null){
                    val celsiusValueMax = deviceData.temperatureData.maxTemperature
                    val celsiusValueMin = deviceData.temperatureData.minTemperature
                    val fahrenheitValueMax = calculateCelsiusToFahrenheit(celsiusValueMax!!)
                    val fahrenheitValueMin = calculateCelsiusToFahrenheit(celsiusValueMin!!)

                    valCurrTempCelsius.text = "0"
                    valCurrTempFahrenheit.text = "32"
                    valMaxTempCelsius.text = modifyDecimalValueToCelsiusString(celsiusValueMax)
                    valMaxTempFahrenheit.text = modifyDecimalValueToFahrenheitString(fahrenheitValueMax)
                    valMinTempCelsius.text = modifyDecimalValueToCelsiusString(celsiusValueMin)
                    valMinTempFahrenheit.text = modifyDecimalValueToFahrenheitString(fahrenheitValueMin)

                    manageUIChangesBasedOnTemperatureData(celsiusValueMax)
                }
            }
        }
    }

    private fun observeTemperatureData() {
        temperatureViewModel.temperatureData.observe(viewLifecycleOwner, Observer { tempData ->
            if (tempData != null) {
                with(binding) {
                    val celsiusValue = tempData.temperature
                    val oldCelsiusValueMax = modifyTemperatureStringToDecimalValue(valMaxTempCelsius.text.toString())
                    val oldCelsiusValueMin = modifyTemperatureStringToDecimalValue(valMinTempCelsius.text.toString())
                    val celsiusValueMax = getHigherTemperature(oldCelsiusValueMax, celsiusValue!!)
                    val celsiusValueMin = getLowerTemperature(oldCelsiusValueMin, celsiusValue)
                    val fahrenheitValue = calculateCelsiusToFahrenheit(celsiusValue)
                    val fahrenheitValueMax = calculateCelsiusToFahrenheit(celsiusValueMax)
                    val fahrenheitValueMin = calculateCelsiusToFahrenheit(celsiusValueMin)

                    valCurrTempCelsius.text = modifyDecimalValueToCelsiusString(celsiusValue)
                    valCurrTempFahrenheit.text = modifyDecimalValueToFahrenheitString(fahrenheitValue)
                    valMaxTempCelsius.text = modifyDecimalValueToCelsiusString(celsiusValueMax)
                    valMaxTempFahrenheit.text = modifyDecimalValueToFahrenheitString(fahrenheitValueMax)
                    valMinTempCelsius.text = modifyDecimalValueToCelsiusString(celsiusValueMin)
                    valMinTempFahrenheit.text = modifyDecimalValueToFahrenheitString(fahrenheitValueMin)

                    manageUIChangesBasedOnTemperatureData(celsiusValue)

                    val temperatureData = TemperatureModel(
                        null,
                        celsiusValueMax,
                        celsiusValueMin
                    )

                    updateDeviceData(temperatureData)
                }
            } else {
                with(binding){
                    valTempText.text = getString(R.string.pet_heat_no_value)
                    textDescription.text = getString(R.string.pet_no_connection)
                }
            }
        })
    }

    private fun updateDeviceData(temperatureData: TemperatureModel) {
        userViewModel.updateTemperatureData(temperatureData)
    }

    private fun calculateCelsiusToFahrenheit(celsius: Double): Double{
        return (celsius * 9 / 5) + 32
    }

    private fun modifyDecimalValueToFahrenheitString(decimalValue: Double): String{
        return String.format("%.2f", decimalValue) + "°F"
    }

    private fun modifyDecimalValueToCelsiusString(decimalValue: Double): String{
        return String.format("%.2f", decimalValue) + "°C"
    }

    private fun modifyTemperatureStringToDecimalValue(temperatureString: String): Double{
        return temperatureString.replace(Regex("°[CF]"), "").toDouble()
    }

    private fun getHigherTemperature(oldCelsius: Double, newCelsius: Double): Double {
        return if (oldCelsius == 0.00) newCelsius else maxOf(oldCelsius, newCelsius)
    }

    private fun getLowerTemperature(oldCelsius: Double, newCelsius: Double): Double {
        return if (oldCelsius == 0.00) newCelsius else minOf(oldCelsius, newCelsius)
    }

    private fun manageUIChangesBasedOnTemperatureData(celsius: Double) {
        with(binding){
            if(celsius > 29.4){
                valTempText.text = getString(R.string.pet_heat_high)
                textDescription.text = getString(R.string.pet_heat_high_desc)
            } else if (celsius < 7.82) {
                valTempText.text = getString(R.string.pet_heat_low)
                textDescription.text = getString(R.string.pet_heat_low_desc)
            } else {
                valTempText.text = getString(R.string.pet_heat_normal)
                textDescription.text = getString(R.string.pet_heat_normal_desc)
            }
        }
    }
}
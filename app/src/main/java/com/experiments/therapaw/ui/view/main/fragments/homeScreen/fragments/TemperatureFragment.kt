package com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.experiments.therapaw.R
import com.experiments.therapaw.data.model.DelayedTempTimeDataModel
import com.experiments.therapaw.data.model.TemperatureModel
import com.experiments.therapaw.data.viewmodel.DevicesViewModel
import com.experiments.therapaw.data.viewmodel.TemperatureViewModel
import com.experiments.therapaw.data.viewmodel.UserViewModel
import com.experiments.therapaw.databinding.FragmentTemperatureBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class TemperatureFragment : Fragment() {

    private lateinit var binding: FragmentTemperatureBinding

    private lateinit var chart: LineChart

    private lateinit var temperatureViewModel: TemperatureViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var devicesViewModel: DevicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTemperatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        temperatureViewModel = TemperatureViewModel()
        userViewModel = UserViewModel()
        devicesViewModel = DevicesViewModel()

        bind()
        chart = binding.chart

        userViewModel.temperature3minLiveData.observe(viewLifecycleOwner) { tempList ->
            if (!tempList.isNullOrEmpty() && binding.navbar.selectedItemId == R.id.menu_3min) {
                updateChart(tempList, 180f, 18f)
            }
        }
        userViewModel.temperature10minLiveData.observe(viewLifecycleOwner) { tempList ->
            if (!tempList.isNullOrEmpty() && binding.navbar.selectedItemId == R.id.menu_10min) {
                updateChart(tempList, 600f, 60f)
            }
        }
        userViewModel.temperature1hrLiveData.observe(viewLifecycleOwner) { tempList ->
            if (!tempList.isNullOrEmpty() && binding.navbar.selectedItemId == R.id.menu_1hr) {
                updateChart(tempList, 3600f, 360f)
            }
        }

        binding.navbar.setOnItemSelectedListener { item ->
            Log.d("selected", "Selected Item: ${item.title}")

            chart.clear()

            when (item.itemId) {
                R.id.menu_3min -> {
                    userViewModel.temperature3minLiveData.value?.let { updateChart(it, 180f, 18f) }
                }
                R.id.menu_10min -> {
                    userViewModel.temperature10minLiveData.value?.let { updateChart(it, 600f, 60f) }
                }
                R.id.menu_1hr -> {
                    userViewModel.temperature1hrLiveData.value?.let { updateChart(it, 3600f, 360f) }
                }
            }
            true
        }

        userViewModel.fetchDeviceData()
    }

    private fun bind() {

        initializeTemperatureData()

        with(binding) {

            switchDevice.setOnClickListener {

                devicesViewModel.toggleTemperatureTracking(switchDevice.isChecked)

                if(!switchDevice.isChecked){
                    val backgroundColor = ColorStateList.valueOf(getColor(requireContext(), R.color.backgroundDisabled))
                    val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textDisabled))

                    valTempText.text = getString(R.string.pet_heat_no_value)
                    textDescription.text = getString(R.string.device_is_currently_turned_off)
                    txtWarning.text = getString(R.string.device_is_currently_turned_off)
                    layoutWarning.visibility = View.VISIBLE
                    cardTemperature.backgroundTintList = backgroundColor
                    cardBackgroundTemperatureText.backgroundTintList = backgroundColor
                    valTempText.setTextColor(textColor)
                } else {

                    layoutWarning.visibility = View.GONE
                }
            }
        }

        observeTemperatureData()
    }

    private fun updateChart(tempList: List<DelayedTempTimeDataModel>, axisMax: Float, extraTime: Float) {
        if (tempList.isEmpty()) return

        val values = ArrayList<Entry>()

        val uniqueSortedList = tempList.sortedBy { it.time }.distinctBy { it.time }

        uniqueSortedList.forEach { tempData ->
            values.add(Entry(tempData.time?.toFloat()!! + extraTime, tempData.temperature!!))
        }

        val dataSet = LineDataSet(values, "Celsius").apply {
            color = getColor(requireContext(), R.color.textRegular)
            setCircleColor(getColor(requireContext(), R.color.textCold))
            valueTextColor = getColor(requireContext(), R.color.textCold)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.15f
            lineWidth = 2f
            circleRadius = 3f
            setDrawValues(false)
        }

        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.setDrawGridBackground(false)
        chart.legend.isEnabled = true
        chart.axisLeft.isEnabled = true
        chart.axisRight.isEnabled = false
        chart.xAxis.isEnabled = true
        chart.axisLeft.textColor = getColor(requireContext(), R.color.text)
        chart.xAxis.textColor = getColor(requireContext(), R.color.text)
        chart.legend.textColor = getColor(requireContext(), R.color.text)
        chart.setNoDataTextColor(getColor(requireContext(), R.color.textCold))
        chart.setBackgroundColor(getColor(requireContext(), R.color.backgroundGraph))
        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = axisMax

        chart.clear()
        chart.data = LineData(dataSet)
        chart.invalidate()
    }

    private fun initializeTemperatureData() {

        with(binding) {

            userViewModel.fetchDailyRecord { dailyRecord ->

                devicesViewModel.fetchDeviceDataTracking { deviceStatus ->

                    switchDevice.isChecked = deviceStatus.temperatureData?.isActive ?: false

                    val celsiusValueMax = dailyRecord?.temperatureData?.maxTemperature!!
                    val celsiusValueMin = dailyRecord.temperatureData?.minTemperature!!
                    val fahrenheitValueMax = calculateCelsiusToFahrenheit(celsiusValueMax)
                    val fahrenheitValueMin = calculateCelsiusToFahrenheit(celsiusValueMin)

                    valMaxTempCelsius.text = modifyDecimalValueToCelsiusString(celsiusValueMax)
                    valMaxTempFahrenheit.text = modifyDecimalValueToFahrenheitString(fahrenheitValueMax)
                    valMinTempCelsius.text = modifyDecimalValueToCelsiusString(celsiusValueMin)
                    valMinTempFahrenheit.text = modifyDecimalValueToFahrenheitString(fahrenheitValueMin)

                    temperatureViewModel.fetchTemperatureData()
                }
            }
        }
    }

    private fun observeTemperatureData() {

        temperatureViewModel.temperatureData.observe(viewLifecycleOwner) { tempData ->

            devicesViewModel.fetchDeviceDataTracking { deviceStatus ->

                if (tempData != null && deviceStatus.temperatureData?.isActive == true) {

                    manageUIChangesBasedOnTemperatureData(tempData)
                } else {

                    with(binding) {
                        val backgroundColor = ColorStateList.valueOf(getColor(requireContext(), R.color.backgroundDisabled))
                        val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textDisabled))

                        valTempText.text = getString(R.string.pet_heat_no_value)
                        textDescription.text = getString(R.string.device_is_currently_turned_off)
                        txtWarning.text = getString(R.string.device_is_currently_turned_off)
                        layoutWarning.visibility = View.VISIBLE
                        cardTemperature.backgroundTintList = backgroundColor
                        cardBackgroundTemperatureText.backgroundTintList = backgroundColor
                        valTempText.setTextColor(textColor)
                    }
                }
            }
        }
    }

    private fun manageUIChangesBasedOnTemperatureData(tempData: TemperatureModel) {

        with(binding) {

            if (tempData.temperature!! > 29.4) {

                val backgroundColor = ColorStateList.valueOf(getColor(requireContext(), R.color.backgroundWarm))
                val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textWarm))

                valTempText.text = getString(R.string.pet_heat_high)
                textDescription.text = getString(R.string.pet_heat_high_desc)
                valTempText.setTextColor(textColor)
                cardTemperature.backgroundTintList = backgroundColor
                cardBackgroundTemperatureText.backgroundTintList = backgroundColor
                imgDescription.visibility = View.VISIBLE
            } else if (tempData.temperature!! < 7.82) {

                val backgroundColor = ColorStateList.valueOf(getColor(requireContext(), R.color.backgroundCold))
                val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textCold))

                valTempText.text = getString(R.string.pet_heat_low)
                textDescription.text = getString(R.string.pet_heat_low_desc)
                valTempText.setTextColor(textColor)
                cardTemperature.backgroundTintList = backgroundColor
                cardBackgroundTemperatureText.backgroundTintList = backgroundColor
                imgDescription.visibility = View.VISIBLE
            } else {

                val backgroundColor = ColorStateList.valueOf(getColor(requireContext(), R.color.backgroundRegular))
                val textColor = ColorStateList.valueOf(getColor(requireContext(), R.color.textRegular))

                valTempText.text = getString(R.string.pet_heat_normal)
                textDescription.text = getString(R.string.pet_heat_normal_desc)
                valTempText.setTextColor(textColor)
                cardTemperature.backgroundTintList = backgroundColor
                cardBackgroundTemperatureText.backgroundTintList = backgroundColor
                imgDescription.visibility = View.GONE
            }

            val celsiusValue = tempData.temperature ?: 0.0
            val oldCelsiusValueMax = modifyTemperatureStringToDecimalValue(valMaxTempCelsius.text.toString())
            val oldCelsiusValueMin = modifyTemperatureStringToDecimalValue(valMinTempCelsius.text.toString())
            val celsiusValueMax = getHigherTemperature(oldCelsiusValueMax, celsiusValue)
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

            val temperatureModel = TemperatureModel(
                maxTemperature = celsiusValueMax,
                minTemperature = celsiusValueMin
            )

            updateDeviceData(temperatureModel)
        }
    }

    private fun updateDeviceData(temperatureData: TemperatureModel) {
        userViewModel.updateTemperatureData(temperatureData)
    }

    private fun calculateCelsiusToFahrenheit(celsius: Double): Double {
        return (celsius * 9 / 5) + 32
    }

    private fun modifyDecimalValueToFahrenheitString(decimalValue: Double): String {
        return String.format("%.2f", decimalValue) + "°F"
    }

    private fun modifyDecimalValueToCelsiusString(decimalValue: Double): String {
        return String.format("%.2f", decimalValue) + "°C"
    }

    private fun modifyTemperatureStringToDecimalValue(temperatureString: String): Double {
        return temperatureString.replace(Regex("°[CF]"), "").toDouble()
    }

    private fun getHigherTemperature(oldCelsius: Double, newCelsius: Double): Double {
        return if (oldCelsius == 0.00) newCelsius else maxOf(oldCelsius, newCelsius)
    }

    private fun getLowerTemperature(oldCelsius: Double, newCelsius: Double): Double {
        return if (oldCelsius == 0.00) newCelsius else minOf(oldCelsius, newCelsius)
    }
}
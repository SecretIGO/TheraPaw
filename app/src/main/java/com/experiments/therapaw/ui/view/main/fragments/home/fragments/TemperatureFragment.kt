package com.experiments.therapaw.ui.view.main.fragments.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.FragmentTemperatureBinding
import com.experiments.therapaw.ui.view.main.fragments.home.fragments.viewmodels.TemperatureViewModel

class TemperatureFragment : Fragment() {

    private lateinit var binding : FragmentTemperatureBinding
    private lateinit var temperatureViewModel: TemperatureViewModel

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

        observeTemperatureData()
        temperatureViewModel.fetchTemperatureData()

        bind()
    }

    fun bind(){
    }

    private fun observeTemperatureData() {
        temperatureViewModel.temperatureData.observe(viewLifecycleOwner, Observer { tempData ->
            if (tempData != null) {
                val celsiusValue = tempData.temperature - 2.0
                val fahrenheitValue = calculateCelsiusToFahrenheit(celsiusValue)

                binding.valCurrTempCelsius.text = String.format("%.2f", celsiusValue) + "°C"
                binding.valCurrTempFahrenheit.text = String.format("%.2f", fahrenheitValue) + "°F"
            }
        })
    }

    private fun calculateCelsiusToFahrenheit(celsius: Double): Double{
        return (celsius * 9 / 5) + 32
    }
}
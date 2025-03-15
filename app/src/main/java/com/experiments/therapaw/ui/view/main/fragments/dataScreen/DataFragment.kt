package com.experiments.therapaw.ui.view.main.fragments.dataScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.experiments.therapaw.R
import com.experiments.therapaw.data.viewmodel.DevicesViewModel
import com.experiments.therapaw.data.viewmodel.UserViewModel
import com.experiments.therapaw.databinding.FragmentDataBinding
import com.experiments.therapaw.databinding.FragmentTemperatureBinding

class DataFragment : Fragment() {

    private lateinit var binding: FragmentDataBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var devicesViewModel: DevicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = UserViewModel()

        bind()
    }

    private fun bind(){
        userViewModel.fetchDailyRecord { dailyRecord ->
            with(binding){
                valMaxTemp.text = dailyRecord?.temperatureData?.maxTemperature.toString()
                valMinTemp.text = dailyRecord?.temperatureData?.minTemperature.toString()
            }
        }
    }
}
package com.experiments.therapaw.ui.view.main.fragments.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.FragmentHeartbeatBinding
import com.experiments.therapaw.databinding.FragmentTemperatureBinding
import com.experiments.therapaw.ui.view.main.fragments.home.fragments.viewmodels.HeartbeatViewModel
import com.experiments.therapaw.ui.view.main.fragments.home.fragments.viewmodels.TemperatureViewModel

class HeartbeatFragment : Fragment() {

    private lateinit var binding : FragmentHeartbeatBinding
    private lateinit var heartbeatViewModel: HeartbeatViewModel

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

        observeHeartbeatData()
        heartbeatViewModel.fetchHeartbeatData()

        bind()
    }

    fun bind(){
    }

    private fun observeHeartbeatData() {
        heartbeatViewModel.heartbeatData.observe(viewLifecycleOwner) { heartbeatData ->
            if (heartbeatData != null) {
                val BPMValue = heartbeatData.bpm;
                val averageBPMValue = heartbeatData.average_bpm;

                binding.valBpm.text = averageBPMValue.toString();

            }
        }
    }
}
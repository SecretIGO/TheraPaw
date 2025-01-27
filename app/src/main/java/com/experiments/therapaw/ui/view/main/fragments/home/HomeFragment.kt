package com.experiments.therapaw.ui.view.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.FragmentHomeBinding
import com.experiments.therapaw.ui.view.main.fragments.home.fragments.HeartbeatFragment
import com.experiments.therapaw.ui.view.main.fragments.home.fragments.LocationFragment
import com.experiments.therapaw.ui.view.main.fragments.home.fragments.TemperatureFragment
import com.experiments.therapaw.viewmodel.SharedViewModel

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = SharedViewModel()

        bind()

    }

    private fun bind(){
        replaceFragment(TemperatureFragment())

        binding.navbar.setOnItemSelectedListener {menuItem ->
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.temperature -> TemperatureFragment()
                R.id.location -> LocationFragment()
                R.id.heartbeat -> HeartbeatFragment()
                else -> TemperatureFragment()
            }
            replaceFragment(fragment)
        }
    }

    private fun replaceFragment(fragment : Fragment): Boolean {
        val fragmentManager = childFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_cont)

        if (currentFragment != null && currentFragment::class.java == fragment::class.java) {
            return false
        }

        val fragmentName = fragment::class.java.simpleName
        when (fragmentName) {
            "TemperatureFragment" -> sharedViewModel.setToolbarTitle("Temperature")
            "LocationFragment" -> sharedViewModel.setToolbarTitle("Location")
            "HeartbeatFragment" -> sharedViewModel.setToolbarTitle("Heartbeat")
        }

        fragmentManager.beginTransaction()
            .replace(R.id.fragment_cont, fragment)
            .commit()

        return true
    }


}
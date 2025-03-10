package com.experiments.therapaw.ui.view.main.fragments.homeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.FragmentHomeBinding
import com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments.HeartbeatFragment
import com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments.LocationFragment
import com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments.TemperatureFragment
import com.experiments.therapaw.data.viewmodel.SharedViewModel
import com.github.mikephil.charting.charts.LineChart

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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

        sharedViewModel.menuActive.observe(requireActivity()) { activeMenu ->
            Log.d("Menu", "Active menu: ${activeMenu.joinToString(", ")}")
        }

        bind()
    }

    private fun bind() {
        replaceFragment(TemperatureFragment())

        bindNavigation()

        binding.navbar.setOnItemSelectedListener { menuItem ->
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.temperature -> TemperatureFragment()
                R.id.location -> LocationFragment()
                R.id.heartbeat -> HeartbeatFragment()
                else -> TemperatureFragment()
            }
            replaceFragment(fragment)
        }
    }

    private fun bindNavigation() {
        when (sharedViewModel.menuActive.value?.getOrNull(1)) {
            "Temperature" -> replaceFragment(TemperatureFragment())
            "Location" -> replaceFragment(LocationFragment())
            "Heartbeat" -> replaceFragment(HeartbeatFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        val fragmentManager = childFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_cont)

        if (currentFragment != null && currentFragment::class.java == fragment::class.java) {
            return false
        }

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_cont, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

        val fragmentName = fragment::class.java.simpleName
        when (fragmentName) {
            "TemperatureFragment" -> {
                sharedViewModel.setMenuActive(arrayOf("Home", "Temperature"))
                sharedViewModel.setToolbarTitle("Temperature")
                Log.d("Menu", "Toolbar title: ${sharedViewModel.toolbarTitle.value}")
            }
            "LocationFragment" -> {
                sharedViewModel.setMenuActive(arrayOf("Home", "Location"))
                sharedViewModel.setToolbarTitle("Location")
                Log.d("Menu", "Toolbar title: ${sharedViewModel.toolbarTitle.value}")
            }
            "HeartbeatFragment" -> {
                sharedViewModel.setMenuActive(arrayOf("Home", "Heartbeat"))
                sharedViewModel.setToolbarTitle("Heartbeat")
                Log.d("Menu", "Toolbar title: ${sharedViewModel.toolbarTitle.value}")
            }
        }

        return true
    }
}
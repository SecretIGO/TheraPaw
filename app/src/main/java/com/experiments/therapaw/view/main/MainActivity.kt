package com.experiments.therapaw.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.ActivityMainBinding
import com.experiments.therapaw.databinding.GenNavbarBinding
import com.experiments.therapaw.databinding.GenToolbarBinding
import com.experiments.therapaw.view.main.fragments.data.DataFragment
import com.experiments.therapaw.view.main.fragments.devices.DevicesFragment
import com.experiments.therapaw.view.main.fragments.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var toolbar : GenToolbarBinding
    private lateinit var navbar : GenNavbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbar = GenToolbarBinding.bind(binding.root)
        navbar = GenNavbarBinding.bind(binding.root)

        setContentView(binding.root)

        bind()
    }

    private fun bind(){
        replaceFragment(HomeFragment())

        navbar.navbar.setOnItemSelectedListener {menuItem ->
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.home -> HomeFragment()
                R.id.devices -> DevicesFragment()
                R.id.data -> DataFragment()
                else -> HomeFragment()
            }
            replaceFragment(fragment)
        }
    }

    private fun replaceFragment(fragment : Fragment): Boolean {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_cont)

        if (currentFragment != null && currentFragment::class.java == fragment::class.java) {
            return false
        }

        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.fragment_cont, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        return true
    }
}
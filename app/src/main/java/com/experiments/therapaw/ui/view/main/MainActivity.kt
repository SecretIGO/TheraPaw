package com.experiments.therapaw.ui.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.ActivityMainBinding
import com.experiments.therapaw.databinding.GenAppdrawerBinding
import com.experiments.therapaw.databinding.GenNavbarBinding
import com.experiments.therapaw.databinding.GenToolbarBinding
import com.experiments.therapaw.ui.view.auth.SignUpActivity
import com.experiments.therapaw.ui.view.main.fragments.data.DataFragment
import com.experiments.therapaw.ui.view.main.fragments.devices.DevicesFragment
import com.experiments.therapaw.ui.view.main.fragments.home.HomeFragment
import com.experiments.therapaw.ui.view.profile.PetProfileActivity
import com.experiments.therapaw.ui.view.profile.ProfileActivity
import com.experiments.therapaw.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: GenToolbarBinding
    private lateinit var navbar: GenNavbarBinding
    private lateinit var drawer: GenAppdrawerBinding
    private lateinit var appdrawer: ActionBarDrawerToggle
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbar = GenToolbarBinding.bind(binding.toolbar.root)
        navbar = GenNavbarBinding.bind(binding.bottomNavbar.root)
        drawer = GenAppdrawerBinding.bind(binding.appdrawer.root)
        sharedViewModel = SharedViewModel()

        setContentView(binding.root)

        sharedViewModel.toolbarTitle.observe(this) { title ->
            toolbar.title.text = title
        }

        bind()
    }

    private fun bind() {
        replaceFragment(HomeFragment())

        toolbar.petProfile.setOnClickListener {
            PetProfileActivity.launch(this)
        }

        navbar.navbar.setOnItemSelectedListener { menuItem ->
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.home -> HomeFragment()
                R.id.devices -> DevicesFragment()
                R.id.data -> DataFragment()
                else -> HomeFragment()
            }
            replaceFragment(fragment)
        }

        appdrawer = ActionBarDrawerToggle(
            this, binding.main, R.string.drawer_open, R.string.drawer_close
        )
        binding.main.addDrawerListener(appdrawer)
        appdrawer.syncState()

        binding.appdrawer.cardProfile.setOnClickListener{
            ProfileActivity.launch(this@MainActivity)
        }

        toolbar.btnAppdrawer.setOnClickListener{ view ->
            (this@MainActivity).openCloseNavigationDrawer(view)
        }

        val homeCard = drawer.navHome
        val submenuHome = drawer.submenuHome
        val homeDropdownArrow = drawer.homeDropdownArrow

        homeCard.setOnClickListener {
            submenuHome.visibility = if (submenuHome.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            homeDropdownArrow.setImageResource(if (submenuHome.visibility == View.VISIBLE) R.drawable.ico_arrow_down else R.drawable.ico_arrow_down)
        }

        val temperatureCard = drawer.navTemperature
        temperatureCard.setOnClickListener {
            Toast.makeText(this, "Temp fragment", Toast.LENGTH_SHORT).show()
        }

        val locationCard = drawer.navLocation
        locationCard.setOnClickListener {
            Toast.makeText(this, "LOC fragment", Toast.LENGTH_SHORT).show()
        }

        val heartbeatCard = drawer.navHeartbeat
        heartbeatCard.setOnClickListener {
            Toast.makeText(this, "BEAT fragment", Toast.LENGTH_SHORT).show()
        }

        val devicesCard = drawer.navDevices
        devicesCard.setOnClickListener {
            Toast.makeText(this, "DEVICES fragment", Toast.LENGTH_SHORT).show()
        }

        val dataCard = drawer.navData
        dataCard.setOnClickListener{
            Toast.makeText(this, "DATA fragment", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCloseNavigationDrawer(view: View) {
        if (binding.main.isDrawerOpen(GravityCompat.START)) {
            binding.main.closeDrawer(GravityCompat.START)
        } else {
            binding.main.openDrawer(GravityCompat.START)
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        val fragmentManager = supportFragmentManager
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
            "HomeFragment" -> sharedViewModel.setToolbarTitle("Home")
            "DevicesFragment" -> sharedViewModel.setToolbarTitle("Devices")
            "DataFragment" -> sharedViewModel.setToolbarTitle("Data")
        }

        return true
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
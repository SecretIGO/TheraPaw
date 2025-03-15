package com.experiments.therapaw.ui.view.main

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.experiments.therapaw.R
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.utils.TimedUploadDataUtils
import com.experiments.therapaw.data.utils.fetchUserData
import com.experiments.therapaw.databinding.ActivityMainBinding
import com.experiments.therapaw.databinding.GenAppdrawerBinding
import com.experiments.therapaw.databinding.GenNavbarBinding
import com.experiments.therapaw.databinding.GenToolbarBinding
import com.experiments.therapaw.data.viewmodel.AuthViewmodel
import com.experiments.therapaw.data.viewmodel.DevicesViewModel
import com.experiments.therapaw.data.viewmodel.HeartbeatViewModel
import com.experiments.therapaw.ui.view.main.fragments.dataScreen.DataFragment
import com.experiments.therapaw.ui.view.main.fragments.devicesScreen.DevicesFragment
import com.experiments.therapaw.ui.view.main.fragments.homeScreen.HomeFragment
import com.experiments.therapaw.ui.view.profile.PetProfileActivity
import com.experiments.therapaw.ui.view.profile.ProfileActivity
import com.experiments.therapaw.data.viewmodel.SharedViewModel
import com.experiments.therapaw.data.viewmodel.TemperatureViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: GenToolbarBinding
    private lateinit var navbar: GenNavbarBinding
    private lateinit var drawer: GenAppdrawerBinding
    private lateinit var appdrawer: ActionBarDrawerToggle
    private lateinit var loopedUpload: TimedUploadDataUtils

    private lateinit var auth: AuthViewmodel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbar = GenToolbarBinding.bind(binding.toolbar.root)
        navbar = GenNavbarBinding.bind(binding.bottomNavbar.root)
        drawer = GenAppdrawerBinding.bind(binding.appdrawer.root)

        auth = AuthViewmodel()
        sharedViewModel = SharedViewModel()
        loopedUpload = TimedUploadDataUtils()

        setContentView(binding.root)
        loopedUpload.delayedUpload()

        sharedViewModel.toolbarTitle.observe(this) { title ->
            toolbar.title.text = title
        }

        bind()
    }

    private fun bind() {
        fetchUserData(this@MainActivity) { userInfo ->
            onUserDataFetched(userInfo)
        }

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

        binding.appdrawer.cardProfile.setOnClickListener {
            ProfileActivity.launch(this@MainActivity)
        }

        toolbar.btnAppdrawer.setOnClickListener { view ->
            (this@MainActivity).openCloseNavigationDrawer(view)
        }

        val homeCard = drawer.navHome
        val submenuHome = drawer.submenuHome
        val homeDropdownArrow = drawer.homeDropdownArrow

        homeCard.setOnClickListener {
            submenuHome.visibility =
                if (submenuHome.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            homeDropdownArrow.setImageResource(if (submenuHome.visibility == View.VISIBLE) R.drawable.ico_arrow_down else R.drawable.ico_arrow_down)
        }

        bindNavigation()
        bindNavigationSettings()

        drawer.navHome.setOnClickListener{
            replaceFragment(HomeFragment())
            sharedViewModel.setMenuActive(arrayOf("Home"))
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

        drawer.navDevices.setOnClickListener{
            replaceFragment(DevicesFragment())
            sharedViewModel.setMenuActive(arrayOf("Devices"))
        }

        drawer.navData.setOnClickListener {
            replaceFragment(DataFragment())
            sharedViewModel.setMenuActive(arrayOf("Data"))
        }
    }

    private fun bindNavigationSettings() {
        val visible = View.VISIBLE
        val invisible = View.GONE
        val selectedBackground = ColorStateList.valueOf(getColor(R.color.primary))
        val selectedTextColor = getColor(R.color.white)
        val selectedIconColor = ColorStateList.valueOf(getColor(R.color.white))

        sharedViewModel.menuActive.observe(this@MainActivity) { activeMenu ->
            defaultSettings()
            Log.d("Menu", "Active menu: ${activeMenu.joinToString(", ")}")
            when {
                activeMenu.contentEquals(arrayOf("Home")) -> {
                    drawer.navHome.backgroundTintList = selectedBackground
                    drawer.submenuHome.visibility = visible
                    drawer.lblHome.setTextColor(selectedTextColor)
                    drawer.homeDropdownArrow.imageTintList = selectedIconColor
                    drawer.iconHome.imageTintList = selectedIconColor

                    when (activeMenu.getOrNull(1)) {
                        "Temperature" -> {
                            drawer.navTemperature.visibility = visible
                            drawer.navTemperature.backgroundTintList = selectedBackground
                        }
                        "Location" -> {
                            drawer.navLocation.visibility = visible
                            drawer.navLocation.backgroundTintList = selectedBackground
                        }
                        "Heartbeat" -> drawer.navHeartbeat.visibility = View.VISIBLE
                    }
                }

                activeMenu.contentEquals(arrayOf("Devices")) -> {
                    drawer.navDevices.backgroundTintList = selectedBackground
                    drawer.lblDevices.setTextColor(selectedTextColor)
                    drawer.iconDevices.imageTintList = selectedIconColor
                    drawer.submenuHome.visibility = invisible
                }

                activeMenu.contentEquals(arrayOf("Data")) -> {
                    drawer.navData.backgroundTintList = selectedBackground
                    drawer.lblData.setTextColor(selectedTextColor)
                    drawer.iconData.imageTintList = selectedIconColor
                    drawer.submenuHome.visibility = invisible
                }
            }
        }
    }

    private fun bindNavigation() {
        when {
            sharedViewModel.menuActive.value.contentEquals(arrayOf("Home")) -> {
                replaceFragment(HomeFragment())
                sharedViewModel.setToolbarTitle("Home")
            }

            sharedViewModel.menuActive.value.contentEquals(arrayOf("Devices")) ->
                replaceFragment(DevicesFragment())

            sharedViewModel.menuActive.value.contentEquals(arrayOf("Data")) ->
                replaceFragment(DataFragment())
        }
    }

    private fun defaultSettings(){
        val visible = View.VISIBLE
        val invisible = View.GONE
        val defaultBackground = ColorStateList.valueOf(getColor(R.color.background))
        val defaultSubmenuBackground = ColorStateList.valueOf(getColor(R.color.backgroundLight))
        val defaultTextColor = getColor(R.color.text)
        val defaultIconColor = ColorStateList.valueOf(getColor(R.color.text))

        with(binding){
            appdrawer.navHome.backgroundTintList = defaultBackground
            appdrawer.lblHome.setTextColor(defaultTextColor)
            appdrawer.iconHome.imageTintList = defaultIconColor
            appdrawer.homeDropdownArrow.imageTintList = defaultIconColor

            appdrawer.navDevices.backgroundTintList = defaultBackground
            appdrawer.lblDevices.setTextColor(defaultTextColor)
            appdrawer.iconDevices.imageTintList = defaultIconColor

            appdrawer.navData.backgroundTintList = defaultBackground
            appdrawer.lblData.setTextColor(defaultTextColor)
            appdrawer.iconData.imageTintList = defaultIconColor

            appdrawer.navSettings.backgroundTintList = defaultBackground
            appdrawer.lblSettings.setTextColor(defaultTextColor)
            appdrawer.iconSettings.imageTintList = defaultIconColor
        }
    }

    private fun onUserDataFetched(userInfo: UserModel) {
        with(binding) {
            appdrawer.textUsername.text = userInfo.username
            appdrawer.textEmail.text = userInfo.email
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
            "HomeFragment" -> {
                sharedViewModel.setMenuActive(arrayOf("Home"))
                sharedViewModel.setToolbarTitle("Home")
            }
            "DevicesFragment" -> {
                sharedViewModel.setMenuActive(arrayOf("Devices"))
                sharedViewModel.setToolbarTitle("Devices")
            }
            "DataFragment" -> {
                sharedViewModel.setMenuActive(arrayOf("Data"))
                sharedViewModel.setToolbarTitle("Data")
            }
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

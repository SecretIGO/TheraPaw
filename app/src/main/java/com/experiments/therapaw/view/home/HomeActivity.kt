package com.experiments.therapaw.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.experiments.therapaw.databinding.ActivityHomeBinding
import com.experiments.therapaw.databinding.GenNavbarBinding
import com.experiments.therapaw.databinding.GenToolbarBinding
import com.experiments.therapaw.R
import com.experiments.therapaw.view.home.fragments.TemperatureFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var toolbar : GenToolbarBinding
    private lateinit var navbar : GenNavbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        toolbar = GenToolbarBinding.bind(binding.root)
        navbar = GenNavbarBinding.bind(binding.root)

        setContentView(binding.root)


    }

//    private fun bind(){
//        with(binding){
//            replaceFragment(TemperatureFragment())
//        }
//    }

//    private fun replaceFragment(fragment : Fragment): Boolean {
//        val fragmentManager = supportFragmentManager
//        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_cont)
//
//        if (currentFragment != null && currentFragment::class.java == fragment::class.java) {
//            return false
//        }
//
//        val transaction = fragmentManager.beginTransaction()
//
//        transaction.replace(R.id.fragment_cont, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//        return true
//    }
}
package com.experiments.therapaw.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.ActivityProfileBinding
import com.experiments.therapaw.utils.dpToPx
import com.experiments.therapaw.view.profile.adapters.ProfileAdapter
import kotlin.math.abs

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bind()
    }

    private fun bind(){
        bindRecyclerView()

        with(binding){
            btnBack.setOnClickListener{
                finish()
            }
        }
    }

    private fun bindRecyclerView(){
        with(binding){
            val layoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            rvProfiles.layoutManager = layoutManager
            rvProfiles.adapter = ProfileAdapter()

            val cardWidth = 225.dpToPx(this@ProfileActivity)
            val padding = cardWidth / 2

            rvProfiles.setPadding(padding, 0, padding, 0)
            rvProfiles.clipToPadding = false

            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(rvProfiles)

            rvProfiles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    scaleCenterItem(recyclerView)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    scaleCenterItem(recyclerView)
                }
            })

            rvProfiles.post {
                if ((rvProfiles.adapter?.itemCount ?: 0) > 2) {
                    rvProfiles.scrollToPosition(1)
                    scaleCenterItem(rvProfiles)
                }
            }
        }
    }

    private fun scaleCenterItem(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val center = recyclerView.width / 2

        for (i in 0 until layoutManager.childCount) {
            val child = layoutManager.getChildAt(i) ?: continue
            val childCenter = (child.left + child.right) / 2
            val distanceFromCenter = abs(center - childCenter).toFloat()
            val scale: Float
            val opacity: Float

            when (layoutManager.getPosition(child)) {
                0 -> {
                    scale = 0.5f + 0.5f * (1 - distanceFromCenter / center)
                    opacity = 0.5f + 0.5f * (1 - distanceFromCenter / center)
                    if (layoutManager.itemCount <= 2){
                        child.background?.setTint(resources.getColor(R.color.primary))
                    } else {
                        child.background?.setTint(resources.getColor(R.color.backgroundRegular))
                        child.setOnClickListener{
                            Toast.makeText(this, "Add User", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                1 -> {
                    scale = 0.5f + 0.5f * (1 - distanceFromCenter / center)
                    opacity = 0.5f + 0.5f * (1 - distanceFromCenter / center)
                    if (layoutManager.itemCount <= 2){
                        child.background?.setTint(resources.getColor(R.color.backgroundRegular))
                        child.setOnClickListener{
                            Toast.makeText(this, "Add User", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        child.background?.setTint(resources.getColor(R.color.primary))
                    }
                }
                else -> {
                    scale = 0.5f + 0.5f * (1 - distanceFromCenter / center)
                    opacity = 0.5f + 0.5f * (1 - distanceFromCenter / center)
                    child.background?.setTint(resources.getColor(R.color.backgroundCold))
                }
            }

            child.scaleX = scale
            child.scaleY = scale
            child.alpha = opacity.coerceIn(0.3f, 1.0f)
        }
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}
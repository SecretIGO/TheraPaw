package com.experiments.therapaw.ui.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.experiments.therapaw.R
import com.experiments.therapaw.data.utils.dpToPx
import com.experiments.therapaw.databinding.ActivityPetProfileBinding
import com.experiments.therapaw.ui.adapter.PetProfileAdapter
import kotlin.math.abs

class PetProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetProfileBinding.inflate(layoutInflater)

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

    private fun bindRecyclerView() {
        with(binding) {
            val layoutManager =
                LinearLayoutManager(this@PetProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            rvProfiles.layoutManager = layoutManager

            val cardWidth = 225.dpToPx(this@PetProfileActivity)
            val padding = cardWidth / 2

            rvProfiles.setPadding(padding, 0, padding, 0)
            rvProfiles.clipToPadding = false

            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(rvProfiles)

            rvProfiles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    scaleCenterItem(recyclerView)
                }
            })

            rvProfiles.adapter = PetProfileAdapter(this@PetProfileActivity)

        }
    }

    private fun scaleCenterItem(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val center = recyclerView.width / 2

        for (i in 0 until layoutManager.childCount) {
            val child = layoutManager.getChildAt(i) ?: continue
            val childCenter = (child.left + child.right) / 2
            val distanceFromCenter = abs(center - childCenter).toFloat()
            val scale = 0.5f + 0.5f * (1 - distanceFromCenter / center)
            val opacity = 0.5f + 0.5f * (1 - distanceFromCenter / center)

            child.scaleX = scale
            child.scaleY = scale
            child.alpha = opacity.coerceIn(0.3f, 1.0f)
        }
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, PetProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}
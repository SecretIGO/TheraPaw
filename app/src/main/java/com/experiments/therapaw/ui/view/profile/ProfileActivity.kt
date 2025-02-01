package com.experiments.therapaw.ui.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.utils.dpToPx
import com.experiments.therapaw.data.utils.fetchUserData
import com.experiments.therapaw.databinding.ActivityProfileBinding
import com.experiments.therapaw.ui.adapter.ProfileAdapter
import com.experiments.therapaw.ui.view.auth.viewmodel.AuthViewmodel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlin.math.abs

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var authViewmodel: AuthViewmodel
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        authViewmodel = AuthViewmodel()
        setContentView(binding.root)

        bind()
    }

    private fun bind() {
        bindRecyclerView()

        fetchUserData (this@ProfileActivity) { userInfo ->
            onUserDataFetched(userInfo)
        }

        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun onUserDataFetched(userInfo: UserModel) {
        with(binding) {
            textUsername.text = userInfo.username
            textEmail.text = userInfo.email
        }
    }

    private fun bindRecyclerView() {
        with(binding) {
            val layoutManager =
                LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            rvProfiles.layoutManager = layoutManager

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

            fetchUserData { userInfo ->
                rvProfiles.adapter = ProfileAdapter(this@ProfileActivity, userInfo, authViewmodel)

                rvProfiles.post {
                    if ((rvProfiles.adapter?.itemCount ?: 0) > 2) {
                        rvProfiles.scrollToPosition(2)
                        scaleCenterItem(rvProfiles)
                    }
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
            val scale = 0.5f + 0.5f * (1 - distanceFromCenter / center)
            val opacity = 0.5f + 0.5f * (1 - distanceFromCenter / center)

            child.scaleX = scale
            child.scaleY = scale
            child.alpha = opacity.coerceIn(0.3f, 1.0f)
        }
    }

    private fun fetchUserData(onUserDataFetched: (UserModel) -> Unit) {
        val authId = auth.uid ?: return

        database.child("users").child(authId).get().addOnSuccessListener { snapshot ->
            val userInfo = snapshot.getValue(UserModel::class.java)

            if (userInfo != null) {
                onUserDataFetched(userInfo)
            } else {
                Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}
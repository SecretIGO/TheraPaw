package com.experiments.therapaw.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.experiments.therapaw.R
import com.experiments.therapaw.data.utils.dpToPx
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.states.AuthenticationStates
import com.experiments.therapaw.ui.view.auth.SignInActivity
import com.experiments.therapaw.ui.view.auth.SignUpActivity
import com.experiments.therapaw.ui.view.auth.viewmodel.AuthViewmodel
import java.io.File

class ProfileAdapter(
    private val userInfo: UserModel,
    private val auth: AuthViewmodel,
    private val context: Context
) : RecyclerView.Adapter<ProfileAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rvitem_profilecard, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.card_profile)

        handleItems(holder, imageView, position)

    }

    private fun handleItems(holder: CardViewHolder, imageView: ImageView, position: Int) {
        when (position) {
            0 -> {
                imageView.setImageResource(R.drawable.ico_logout)
                val params = imageView.layoutParams as ConstraintLayout.LayoutParams
                params.width = 50.dpToPx(holder.itemView.context)
                params.height = 50.dpToPx(holder.itemView.context)
                imageView.layoutParams = params

                imageView.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.ico_primary),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                imageView.setOnClickListener {
                    onLogoutButtonClick()
                }
            }

            1 -> {
                imageView.setImageResource(R.drawable.ico_add)
                val params = imageView.layoutParams as ConstraintLayout.LayoutParams
                params.width = 50.dpToPx(holder.itemView.context)
                params.height = 50.dpToPx(holder.itemView.context)
                imageView.layoutParams = params

                imageView.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.ico_primary),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                imageView.setOnClickListener {
                    onAddButtonClick()
                }
            }

            2 -> {
                userInfo.profilePicture?.let {
                    Glide.with(holder.itemView.context)
                        .load(File(it))
                        .placeholder(R.drawable.bannerlogo_therapaw)
                        .into(imageView)
                }
            }

            else -> {
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }

    override fun getItemCount(): Int = 3

    private fun onAddButtonClick() {
        Toast.makeText(context, "Add button clicked!", Toast.LENGTH_SHORT).show()

        SignUpActivity.launch(context)
    }


    private fun onLogoutButtonClick() {
        Toast.makeText(context, "Logout button clicked!", Toast.LENGTH_SHORT).show()

        auth.logout()
        auth.setAuthState(AuthenticationStates.LogOut)

        val intent = Intent(context, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

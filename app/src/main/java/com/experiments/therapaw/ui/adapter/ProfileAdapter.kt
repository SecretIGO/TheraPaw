package com.experiments.therapaw.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.experiments.therapaw.R
import com.experiments.therapaw.data.utils.dpToPx
import com.experiments.therapaw.data.model.UserModel
import java.io.File

class ProfileAdapter(
    private val userInfo: UserModel,
    private val onAddButtonClick: () -> Unit
) : RecyclerView.Adapter<ProfileAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rvitem_profilecard, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.card_profile)

        when {
            itemCount <= 2 -> handleFewItems(holder, imageView, position)
            itemCount > 2 -> handleManyItems(holder, imageView, position)
        }
    }

    private fun handleFewItems(holder: CardViewHolder, imageView: ImageView, position: Int) {
        when (position) {
            0 -> {
                userInfo.profilePicture?.let {
                    Glide.with(holder.itemView.context)
                        .load(File(it))
                        .placeholder(R.drawable.bannerlogo_therapaw)
                        .error(R.drawable.ic_launcher_background)
                        .into(imageView)
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

                imageView.setOnClickListener { onAddButtonClick() }
            }
        }
    }

    private fun handleManyItems(holder: CardViewHolder, imageView: ImageView, position: Int) {
        when (position) {
            0 -> {
                imageView.setImageResource(R.drawable.ico_add)
                val params = imageView.layoutParams as ConstraintLayout.LayoutParams
                params.width = 50.dpToPx(holder.itemView.context)
                params.height = 50.dpToPx(holder.itemView.context)
                imageView.layoutParams = params

                imageView.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.ico_primary),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                imageView.setOnClickListener { onAddButtonClick() }
            }
            1 -> {
                userInfo.profilePicture?.let {
                    Glide.with(holder.itemView.context)
                        .load(File(it))
                        .placeholder(R.drawable.bannerlogo_therapaw)
                        .error(R.drawable.ic_launcher_background)
                        .into(imageView)
                }
            }
            else -> {
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }

    override fun getItemCount(): Int = 3

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

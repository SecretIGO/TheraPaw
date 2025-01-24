package com.experiments.therapaw.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.experiments.therapaw.R
import com.experiments.therapaw.data.utils.dpToPx

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvitem_profilecard, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val drawableResource = when {
            itemCount <= 2 -> {
                when (position) {
                    0 -> R.drawable.ic_launcher_foreground
                    1 -> R.drawable.ico_add
                    else -> null
                }
            }
            itemCount > 2 -> {
                when (position) {
                    0 -> R.drawable.ico_add
                    1 -> R.drawable.ic_launcher_foreground
                    else -> null
                }
            }
            else -> null
        }

        val imageView = holder.itemView.findViewById<ImageView>(R.id.card_profile)

        if (itemCount <= 2) {
            if(position == 1){
                val params = imageView.layoutParams as ConstraintLayout.LayoutParams
                params.width = 50.dpToPx(holder.itemView.context)
                params.height = 50.dpToPx(holder.itemView.context)
                imageView.layoutParams = params

                imageView.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.ico_primary),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        } else if (itemCount > 2) {
            if(position == 0){
                val params = imageView.layoutParams as ConstraintLayout.LayoutParams
                params.width = 50.dpToPx(holder.itemView.context)
                params.height = 50.dpToPx(holder.itemView.context)
                imageView.layoutParams = params

                imageView.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.ico_primary),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }

        if (drawableResource != null) {
            imageView.setImageResource(drawableResource)
        }
    }

    override fun getItemCount(): Int = 3

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
package com.experiments.therapaw.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.experiments.therapaw.R
import com.experiments.therapaw.data.utils.dpToPx

class PetProfileAdapter(
    private val context: Context
) : RecyclerView.Adapter<PetProfileAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rvitem_profilecard, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.card_profile)

        if (itemCount <= 2) {
            handleSmallItems(holder, imageView, position)
        } else {
            handleManyItems(holder, imageView, position)
        }

    }

    override fun getItemCount(): Int = 1

    private fun handleSmallItems(holder: CardViewHolder, imageView: ImageView, position: Int) {
        when (position) {
            0 -> {
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
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
        }
    }

    private fun handleManyItems(holder: CardViewHolder, imageView: ImageView, position: Int) {
        when (position) {

        }
    }

    private fun onAddButtonClick() {

    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
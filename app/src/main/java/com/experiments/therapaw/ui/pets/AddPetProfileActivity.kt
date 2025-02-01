package com.experiments.therapaw.ui.pets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.experiments.therapaw.R
import com.experiments.therapaw.data.model.PetModel
import com.experiments.therapaw.data.utils.auth
import com.experiments.therapaw.databinding.ActivityAddPetProfileBinding
import com.experiments.therapaw.databinding.ActivityPetProfileBinding
import com.experiments.therapaw.ui.view.profile.PetProfileActivity

class AddPetProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPetProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPetProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bind()
    }

    private fun bind() {
        with(binding) {
            btnAddPet.setOnClickListener {
                val pet = PetModel(
                    uid = "",
                    ownerUID = auth.uid.toString(),
                    petName = textinputPetName.text.toString(),
                    size = textinputPetSize.text.toString(),
                    breed = textinputBreed.text.toString(),
                    weight = textinputWeight.text.toString().toFloat(),
                    height = textinputHeight.text.toString().toFloat(),
                    profilePicture = null
                )


            }
        }
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AddPetProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}
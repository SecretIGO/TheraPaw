package com.experiments.therapaw.ui.view.auth

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.experiments.therapaw.data.model.UserModel
import com.experiments.therapaw.data.states.AuthenticationStates
import com.experiments.therapaw.data.utils.saveImageToInternalStorage
import com.experiments.therapaw.databinding.ActivitySignUpBinding
import com.experiments.therapaw.ui.view.auth.viewmodel.AuthViewmodel
import com.experiments.therapaw.ui.view.auth.viewmodel.UserViewModel
import com.experiments.therapaw.ui.view.main.MainActivity
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: AuthViewmodel
    private lateinit var userViewmodel: UserViewModel
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        auth = AuthViewmodel()
        userViewmodel = UserViewModel()
        setContentView(binding.root)

        bind()
    }

    private fun bind() {
        with(binding) {
            btnImageSelector.setOnClickListener {
                resultLauncher.launch("image/*")
            }
            btnSignup.setOnClickListener {
                signup()
            }
        }
    }

    private fun signup() {
        lifecycleScope.launch {
            try {
                auth.setAuthState(AuthenticationStates.Loading)

                val result = auth.signUp(
                    binding.inputEmail.text.toString(),
                    binding.inputPassword.text.toString()
                )

                if (result == AuthenticationStates.SignedUp) {
                    with(binding) {
                        val bitmap = (binding.imgImageSelector.drawable as BitmapDrawable).bitmap
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)

                        val filePath = saveImageToInternalStorage(
                            this@SignUpActivity,
                            baos.toByteArray(),
                            "${auth.getCurrentUserUid()}.jpg"
                        )

                        if (filePath != null) {
                            val userinfo = UserModel(
                                auth.getCurrentUserUid().toString(),
                                inputUsername.text.toString(),
                                inputEmail.text.toString(),
                                filePath,
                                null
                            )

                            userViewmodel.addUserData(userinfo, baos.toByteArray(), this@SignUpActivity) {
                                auth.setAuthState(AuthenticationStates.UserCreated)

                                if (auth.getAuthStates().value == AuthenticationStates.UserCreated) {
                                    MainActivity.launch(this@SignUpActivity)
                                }
                            }
                        } else {
                            Toast.makeText(this@SignUpActivity, "Failed to save image", Toast.LENGTH_SHORT).show()
                            auth.setAuthState(AuthenticationStates.Error)
                        }
                    }
                } else {
                    auth.setAuthState(AuthenticationStates.Error)
                }
            } catch (e: Exception) {
                auth.setAuthState(AuthenticationStates.Error)
            }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        binding.imgImageSelector.setImageURI(imageUri)
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }
}
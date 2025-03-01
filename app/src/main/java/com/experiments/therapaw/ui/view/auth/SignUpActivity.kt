package com.experiments.therapaw.ui.view.auth

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.experiments.therapaw.data.states.AuthenticationStates
import com.experiments.therapaw.data.utils.createAuthModel
import com.experiments.therapaw.data.utils.createUserModel
import com.experiments.therapaw.data.utils.saveImageToInternalStorage
import com.experiments.therapaw.data.utils.signup
import com.experiments.therapaw.databinding.ActivitySignUpBinding
import com.experiments.therapaw.data.viewmodel.AuthViewmodel
import com.experiments.therapaw.data.viewmodel.UserViewModel
import kotlinx.coroutines.launch

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
                val authValues =
                    createAuthModel(inputEmail.text.toString(), inputPassword.text.toString())
                val userValues = createUserModel(
                    "",
                    inputUsername.text.toString(),
                    inputEmail.text.toString(),
                    ""
                )
                lifecycleScope.launch {
                    try {
                        signup(
                            this@SignUpActivity,
                            bitmap = (imgImageSelector.drawable as? BitmapDrawable)?.bitmap,
                            authValues = authValues,
                            userValues = userValues,
                            auth = auth,
                            saveImageToInternalStorage = ::saveImageToInternalStorage,
                            addUserData = { userInfo, imageBytes, context, onComplete ->
                                userViewmodel.addUserData(userInfo, imageBytes, context, onComplete)
                            }
                        )
                    } catch (e: Exception) {
                        Toast.makeText(this@SignUpActivity, "Signup Failed!", Toast.LENGTH_SHORT).show()
                        auth.setAuthState(AuthenticationStates.Error)
                    }
                }
            }

            btnSwitchToLogin.setOnClickListener {
                Log.d("test", "marco")

                SignInActivity.launch(this@SignUpActivity)
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
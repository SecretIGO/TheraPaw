package com.experiments.therapaw.ui.view.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.experiments.therapaw.data.model.AuthModel
import com.experiments.therapaw.data.states.AuthenticationStates
import com.experiments.therapaw.data.utils.signin
import com.experiments.therapaw.databinding.ActivitySignInBinding
import com.experiments.therapaw.data.viewmodel.AuthViewmodel
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    private lateinit var auth: AuthViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        auth = AuthViewmodel()
        setContentView(binding.root)

        bind()
    }

    private fun bind() {
        with(binding) {
            Log.d("test", "binding")
            btnSignin.setOnClickListener {
                Log.d("test", "click")
                val authValues = AuthModel(inputEmail.text.toString(), inputPassword.text.toString())

                lifecycleScope.launch {
                    try {
                        signin(this@SignInActivity, authValues, auth)
                    } catch (e: Exception) {
                        Toast.makeText(this@SignInActivity, "Signup Failed!", Toast.LENGTH_SHORT).show()
                        auth.setAuthState(AuthenticationStates.Error)
                    }
                }
            }

            btnSwitchToSignup.setOnClickListener {
                Log.d("test", "marco")
                SignUpActivity.launch(this@SignInActivity)
            }
        }
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SignInActivity::class.java)
            context.startActivity(intent)
        }
    }
}
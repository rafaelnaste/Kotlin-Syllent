package com.syllent.connectdev.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.syllent.connectdev.R
import com.syllent.connectdev.databinding.ActivityLoginBinding
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk

/**
 * Login Activity for user authentication with Tuya SDK.
 * Users can login with email and password.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged in
        if (ThingHomeSdk.getUserInstance().isLogin) {
            navigateToMain()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInputs(email, password)) {
                performLogin(email, password)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true

        // Validate email
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.invalid_email)
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        // Validate password
        if (password.isEmpty() || password.length < 6) {
            binding.tilPassword.error = getString(R.string.invalid_password)
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        return isValid
    }

    private fun performLogin(email: String, password: String) {
        showLoading(true)
        hideError()

        // Login with email using Tuya SDK
        // Country code "55" is for Brazil, adjust as needed
        ThingHomeSdk.getUserInstance().loginWithEmail(
            "55", // Country code
            email,
            password,
            object : ILoginCallback {
                override fun onSuccess(user: User?) {
                    runOnUiThread {
                        showLoading(false)
                        Toast.makeText(
                            this@LoginActivity,
                            R.string.login_success,
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToMain()
                    }
                }

                override fun onError(code: String?, error: String?) {
                    runOnUiThread {
                        showLoading(false)
                        showError("${getString(R.string.login_error)}: $error")
                    }
                }
            }
        )
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !show
        binding.btnLogin.text = if (show) {
            getString(R.string.login_loading)
        } else {
            getString(R.string.login_button)
        }
    }

    private fun showError(message: String) {
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = message
    }

    private fun hideError() {
        binding.tvError.visibility = View.GONE
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

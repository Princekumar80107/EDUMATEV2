package com.example.edumate.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.edumate.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailContainer: com.google.android.material.textfield.TextInputLayout
    private lateinit var loginEmail: com.google.android.material.textfield.TextInputEditText
    private lateinit var passwordContainer: com.google.android.material.textfield.TextInputLayout
    private lateinit var loginPassword: com.google.android.material.textfield.TextInputEditText
    private lateinit var loginBtn: androidx.appcompat.widget.AppCompatButton
    private lateinit var loginProgressBar: ProgressBar
    private lateinit var forgetPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        setUpViews()

        forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpViews() {
        findView()
        activityChange()
        userLogin()
    }

    private fun userLogin() {

        loginBtn.setOnClickListener {
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            loginEmail.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    emailContainer.helperText = enterEmail()
                }
            }
            loginPassword.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    passwordContainer.helperText = enterPassword()
                }
            }

            loginProgressBar.visibility = View.VISIBLE

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    loginProgressBar.visibility = View.GONE
                    emailContainer.helperText = "Enter your email"
                }
                if (password.isEmpty()) {
                    loginProgressBar.visibility = View.GONE
                    passwordContainer.helperText = "Enter your password"
                }
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginProgressBar.visibility = View.GONE
                emailContainer.helperText = "Enter valid email address"
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // checking whether user is verified or not
                        val verification = auth.currentUser?.isEmailVerified

                        if(verification == true) {
                            loginProgressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Login Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            loginProgressBar.visibility = View.GONE
                            Toast.makeText(this, "Please verify your email", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        loginProgressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Something went wrong with email or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun enterEmail(): String? {
        val email = loginEmail.text.toString()
        if (email.isEmpty()) {
            return "Enter your email"
        }
        return null
    }

    private fun enterPassword(): String? {
        val password = loginPassword.text.toString()
        if (password.isEmpty()) {
            return "Enter your password"
        }
        return null
    }

    private fun activityChange() {
        val signUpTxt: TextView = findViewById(R.id.signUpTxt)

        signUpTxt.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun findView() {
        emailContainer = findViewById(R.id.loginEmailContainer)
        loginEmail = findViewById(R.id.loginEmailIDEditTxt)
        passwordContainer = findViewById(R.id.loginPasswordContainer)
        loginPassword = findViewById(R.id.loginPasswordEditTxt)
        loginBtn = findViewById(R.id.loginBtn)
        loginProgressBar = findViewById(R.id.progressBarLogin)
        forgetPassword = findViewById(R.id.forgetPasswordTxt)
    }
}
package com.hyunjung.twitterclone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hyunjung.twitterclone.databinding.ActivitySignupBinding
import com.hyunjung.twitterclone.util.DATA_USERS
import com.hyunjung.twitterclone.util.User

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
            startActivity(HomeActivity.newIntent(this))
            finish()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTextChangeListener(binding.usernameET, binding.usernameTIL)
        setTextChangeListener(binding.emailET, binding.emailTIL)
        setTextChangeListener(binding.passwordET, binding.passwordTIL)

        // ProgressBar가 떠있는 동안 touch 안되게 막아놓는 역할
        binding.signupProgressLayout.setOnTouchListener{ v, event -> true }
    }

    private fun setTextChangeListener(et: EditText, til: TextInputLayout) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                til.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun onSignup(v: View) {
        var proceed = true
        if (binding.usernameET.text.isNullOrBlank()) {
            binding.usernameTIL.error = "Username is required"
            binding.usernameTIL.isErrorEnabled = true
            proceed = false
        }
        if (binding.emailET.text.isNullOrBlank()) {
            binding.emailTIL.error = "Email is required"
            binding.emailTIL.isErrorEnabled = true
            proceed = false
        }
        if (binding.passwordET.text.isNullOrBlank()) {
            binding.passwordTIL.error = "Password is required"
            binding.passwordTIL.isErrorEnabled = true
            proceed = false
        }
        if (proceed) {
            binding.signupProgressLayout.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(binding.emailET.text.toString(), binding.passwordET.text.toString())
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Signup error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    } else {
                        val email = binding.emailET.text.toString()
                        val name = binding.usernameET.text.toString()
                        val user = User(email, name, "", arrayListOf(), arrayListOf())
                        firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user) // user 파이어베이스 DB 생성
                    }
                    binding.signupProgressLayout.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    binding.signupProgressLayout.visibility = View.GONE
                }
        }

    }

    fun goToLogin(v: View) {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SignupActivity::class.java)
    }
}
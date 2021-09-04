package com.hyunjung.twitterclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLogin(v: View) {
        Toast.makeText(this, "onLogin", Toast.LENGTH_SHORT).show()
    }

    fun goToSignup(v: View) {
        Toast.makeText(this, "goToSignup", Toast.LENGTH_SHORT).show()
    }
}

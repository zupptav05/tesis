package com.zenboom.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zenboom.app.MainActivity
import com.zenboom.app.data.SessionManager
import com.zenboom.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        // --- EL TRUCO DE PERSISTENCIA ---
        if (sessionManager.isLoggedIn()) {
            goToHome()
            return // Detiene el resto del código para no mostrar el Login
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            // Simulación de validación
            if (email.isNotEmpty()) {
                sessionManager.createLoginSession(email) // Guardamos la sesión
                goToHome()
            }
        }
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
package com.zenboom.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.zenboom.app.MainActivity
import com.zenboom.app.data.SessionManager
import com.zenboom.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(this)

        // 1. SI YA HAY SESIÓN, ENTRAR DIRECTO
        if (sessionManager.isLoggedIn()) {
            goToHome()
            return
        }

        // 2. BOTÓN DE LOGIN (MODO BYPASS / PRUEBA)
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                // --- TRUCO: BYPASS DE FIREBASE ---
                // En lugar de enviar el correo real, simulamos que todo salió bien.
                // Esto es solo para que puedas trabajar hoy.

                // 1. Guardamos una sesión "falsa" localmente para que la app recuerde que entraste
                sessionManager.createLoginSession(email)

                // 2. Mostramos mensaje
                Toast.makeText(this, "Modo Prueba: Acceso concedido a $email", Toast.LENGTH_SHORT).show()

                // 3. Entramos a la App
                goToHome()

            } else {
                binding.etEmail.error = "Escribe cualquier correo para probar"
            }
        }
    }

    // Estas funciones se quedan aquí por si mañana quieres volver a activar el modo real
    // pero por ahora no se usan.
    private fun validarEnlaceMagico(emailLink: String) {
        // Desactivado temporalmente
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Desactivado temporalmente
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
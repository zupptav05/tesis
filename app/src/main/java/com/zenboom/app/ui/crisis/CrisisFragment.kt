package com.zenboom.app.ui.crisis

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.zenboom.app.R
import java.util.Locale

class CrisisFragment : Fragment(R.layout.fragment_crisis), TextToSpeech.OnInitListener {

    private lateinit var tvInstruction: TextView
    private lateinit var breathingCircle: View
    private lateinit var tts: TextToSpeech
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = true

    // Tiempos terapéuticos (4 segundos por fase)
    private val STEP_TIME = 4000L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvInstruction = view.findViewById(R.id.tv_instruction)
        breathingCircle = view.findViewById(R.id.view_breathing_circle)

        // Inicializar motor de voz
        tts = TextToSpeech(requireContext(), this)

        // Botón de Salida
        view.findViewById<View>(R.id.btn_stop_crisis).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Configurar idioma español
            val result = tts.setLanguage(Locale("es", "MX"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts.language = Locale("es")
            }
            // Pequeña pausa antes de empezar para que el usuario se acomode
            handler.postDelayed({ startBreathingCycle() }, 1000)
        }
    }

    private fun startBreathingCycle() {
        if (!isRunning) return

        // --- FASE 1: INHALA (Círculo Crece) ---
        updateUI("Inhala...", "Inhala profundo por la nariz")
        animateCircle(1.0f, 1.8f, STEP_TIME) // Crece al 180%

        handler.postDelayed({
            if (!isRunning) return@postDelayed

            // --- FASE 2: MANTÉN (Círculo Estático) ---
            updateUI("Mantén...", "Mantén el aire")
            // Sin animación, se queda grande

            handler.postDelayed({
                if (!isRunning) return@postDelayed

                // --- FASE 3: EXHALA (Círculo Se Encoge) ---
                updateUI("Exhala...", "Suéltalo despacio por la boca")
                animateCircle(1.8f, 1.0f, STEP_TIME) // Vuelve a tamaño original

                // --- REPETIR ---
                handler.postDelayed({
                    startBreathingCycle()
                }, STEP_TIME)

            }, STEP_TIME)

        }, STEP_TIME)
    }

    private fun updateUI(text: String, speech: String) {
        // Actualiza texto en pantalla
        tvInstruction.text = text
        // Habla
        tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun animateCircle(from: Float, to: Float, duration: Long) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, from, to)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, from, to)
        val animator = ObjectAnimator.ofPropertyValuesHolder(breathingCircle, scaleX, scaleY)
        animator.duration = duration
        animator.interpolator = AccelerateDecelerateInterpolator() // Movimiento natural
        animator.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isRunning = false
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        handler.removeCallbacksAndMessages(null)
    }
}
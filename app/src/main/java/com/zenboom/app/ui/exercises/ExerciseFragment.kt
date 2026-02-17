package com.zenboom.app.ui.exercises

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.zenboom.app.R
import java.util.Locale

class ExerciseFragment : Fragment(R.layout.fragment_exercise), TextToSpeech.OnInitListener {

    private lateinit var tvInstruction: TextView
    private lateinit var lottieView: LottieAnimationView
    private lateinit var tts: TextToSpeech
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvInstruction = view.findViewById(R.id.tv_instruction)
        // Asegúrate de que en tu XML el ID sea animation_view
        lottieView = view.findViewById(R.id.animation_view)

        tts = TextToSpeech(requireContext(), this)

        view.findViewById<View>(R.id.btn_stop).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("es", "MX")
            lottieView.playAnimation()
            startBreathingCycle()
        }
    }

    private fun startBreathingCycle() {
        if (!isRunning) return

        // INHALA (Rápido y Energético)
        tvInstruction.text = "Inhala..."
        speak("Inhala profundo")
        lottieView.speed = 1.0f
        lottieView.resumeAnimation()

        handler.postDelayed({
            if (!isRunning) return@postDelayed

            // MANTÉN (Pausa Visual)
            tvInstruction.text = "Mantén..."
            speak("Mantén el aire")
            lottieView.pauseAnimation()

            handler.postDelayed({
                if (!isRunning) return@postDelayed

                // EXHALA (Lento y Relajante)
                tvInstruction.text = "Exhala..."
                speak("Exhala despacio")
                lottieView.resumeAnimation()
                lottieView.speed = 0.5f // Cámara lenta para relajar

                handler.postDelayed({ startBreathingCycle() }, 4000)

            }, 4000)
        }, 4000)
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isRunning = false
        lottieView.cancelAnimation()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        handler.removeCallbacksAndMessages(null)
    }
}
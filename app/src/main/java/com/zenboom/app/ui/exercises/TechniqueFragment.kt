package com.zenboom.app.ui.exercises

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zenboom.app.R

class TechniqueFragment : Fragment(R.layout.fragment_technique) {
    private var currentStep = 5

    private lateinit var tvNumber: TextView
    private lateinit var tvText: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var cardContainer: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNumber = view.findViewById(R.id.tv_step_number)
        tvText = view.findViewById(R.id.tv_instruction)
        ivIcon = view.findViewById(R.id.iv_sense_icon)
        cardContainer = view.findViewById(R.id.card_step)

        view.findViewById<View>(R.id.btn_next_step).setOnClickListener {
            nextStep()
        }

        view.findViewById<View>(R.id.btn_close_technique).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun nextStep() {
        currentStep--
        if (currentStep > 0) {
            // Animación: Deslizar hacia abajo y desvanecer
            cardContainer.animate()
                .alpha(0f)
                .translationY(50f)
                .setDuration(300)
                .withEndAction {
                    updateUI()
                    // Reaparecer
                    cardContainer.translationY = -50f
                    cardContainer.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(300)
                        .start()
                }.start()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun updateUI() {
        tvNumber.text = currentStep.toString()
        when(currentStep) {
            4 -> {
                tvText.text = "Cosas que puedas tocar"
                ivIcon.setImageResource(android.R.drawable.ic_menu_gallery)
            }
            3 -> {
                tvText.text = "Sonidos que puedas escuchar"
                ivIcon.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
            }
            2 -> {
                tvText.text = "Olores que puedas percibir"
                ivIcon.setImageResource(android.R.drawable.ic_menu_compass)
            }
            1 -> {
                tvText.text = "Algo que puedas saborear"
                ivIcon.setImageResource(android.R.drawable.ic_menu_mylocation)
            }
        }
    }
}
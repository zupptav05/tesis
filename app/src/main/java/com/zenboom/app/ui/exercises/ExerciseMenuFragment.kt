package com.zenboom.app.ui.exercises

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zenboom.app.R

class ExerciseMenuFragment : Fragment(R.layout.fragment_exercise_menu) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ir a Respiración (Usamos el que ya tenías)
        view.findViewById<View>(R.id.btn_breathing).setOnClickListener {
            findNavController().navigate(R.id.action_menu_to_breathing)
        }

        // 2. Ir a Técnica 5-4-3-2-1
        view.findViewById<View>(R.id.btn_54321).setOnClickListener {
            findNavController().navigate(R.id.action_menu_to_54321)
        }

        // 3. Ir a Burbujas
        view.findViewById<View>(R.id.btn_bubbles).setOnClickListener {
            findNavController().navigate(R.id.action_menu_to_bubbles)
        }

        // 4. Regresar al Home
        view.findViewById<View>(R.id.btn_back_home).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
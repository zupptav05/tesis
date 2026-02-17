package com.zenboom.app.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zenboom.app.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Botón CRISIS (Botón Naranja)
        view.findViewById<View>(R.id.card_crisis).setOnClickListener {
            try {
                findNavController().navigate(R.id.nav_crisis)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al ir a Crisis", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Botón EJERCICIOS (Botón Azul Claro)
        // ESTE ES EL CAMBIO: Ahora lleva al Menú de Ejercicios (Respiración, 5-4-3-2-1, Burbujas)
        view.findViewById<View>(R.id.card_ejercicios).setOnClickListener {
            try {
                findNavController().navigate(R.id.nav_exercise_menu)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al ir al menú de ejercicios", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Botón MI PROGRESO (Botón Morado)
        view.findViewById<View>(R.id.card_progreso).setOnClickListener {
            try {
                findNavController().navigate(R.id.nav_progress)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al ir a Progreso", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Botón SALIR (Botón Gris)
        view.findViewById<View>(R.id.card_salir).setOnClickListener {
            activity?.finish()
        }
    }
}
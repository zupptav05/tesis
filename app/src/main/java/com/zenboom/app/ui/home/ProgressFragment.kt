package com.zenboom.app.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.zenboom.app.R
import com.zenboom.app.data.ZenboomDBHelper

// Esta es tu pantalla de estadísticas (antes era Home)
class ProgressFragment : Fragment(R.layout.fragment_progress) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Nota: Asegúrate de que en tu XML copiado los IDs sean correctos
        // Si usabas 'text_home', aquí lo buscamos igual.
        val tvStatus = view.findViewById<TextView>(R.id.text_home)

        try {
            val dbHelper = ZenboomDBHelper(requireContext())
            val allRecords = dbHelper.getAllRecords()

            if (allRecords.isNotEmpty()) {
                val ultimo = allRecords[0]
                tvStatus.text = "Ritmo Cardíaco: ${ultimo.heartRate} BPM\n\nNivel de Estrés: ${ultimo.stressLevel}"
            } else {
                tvStatus.text = "Aún no hay registros.\nVe a 'Ejercicios' o realiza un escaneo."
            }
        } catch (e: Exception) {
            tvStatus.text = "Bienvenido a ZenBom"
        }
    }
}
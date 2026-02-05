package com.zenboom.app.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.zenboom.app.R
import com.zenboom.app.data.*
import kotlinx.coroutines.launch

class AnalysisFragment : Fragment(R.layout.fragment_analysis) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnScan = view.findViewById<Button>(R.id.btn_scan)
        val healthManager = HealthManager(requireContext())
        val db = ZenboomDatabase.getDatabase(requireContext())

        btnScan.setOnClickListener {
            lifecycleScope.launch {
                val (bpm, spo2) = healthManager.fetchLatestVitals()

                // Algoritmo de tesis: Detección Heurística
                val status = when {
                    bpm > 110 && spo2 < 95 -> "Ansiedad Alta"
                    bpm > 90 -> "Estrés Moderado"
                    else -> "Calma Total"
                }

                // Guardar en Historial
                db.anxietyDao().insert(AnxietyRecord(
                    timestamp = System.currentTimeMillis(),
                    heartRate = bpm,
                    oxygen = spo2,
                    stressLevel = status
                ))

                Toast.makeText(context, "Análisis: $status", Toast.LENGTH_LONG).show()
            }
        }
    }
}
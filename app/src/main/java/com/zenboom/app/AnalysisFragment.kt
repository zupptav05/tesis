package com.zenboom.app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.zenboom.app.R
import com.zenboom.app.data.HealthManager
import com.zenboom.app.data.ZenboomDBHelper // Ojo: Importamos tu nueva clase
import kotlinx.coroutines.launch

class AnalysisFragment : Fragment(R.layout.fragment_analysis) {

    // Launcher de permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            realizarAnalisis()
        } else {
            Toast.makeText(requireContext(), "Se requiere permiso para usar los sensores", Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnScan = view.findViewById<Button>(R.id.btn_scan)

        btnScan.setOnClickListener {
            checkPermissionsAndRun()
        }
    }

    private fun checkPermissionsAndRun() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BODY_SENSORS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            realizarAnalisis()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS)
        }
    }

    private fun realizarAnalisis() {
        // 1. Inicializamos tu base de datos nueva (Helper)
        val dbHelper = ZenboomDBHelper(requireContext())
        val healthManager = HealthManager(requireContext())

        lifecycleScope.launch {
            try {
                Toast.makeText(context, "Leyendo sensores...", Toast.LENGTH_SHORT).show()
                val (bpm, spo2) = healthManager.fetchLatestVitals()

                val status = when {
                    bpm > 110 && spo2 < 95 -> "Ansiedad Alta"
                    bpm > 90 -> "Estrés Moderado"
                    else -> "Calma Total"
                }

                // 2. Usamos el método insertRecord de TU código nativo
                dbHelper.insertRecord(hr = bpm, oxygen = spo2, stress = status)

                Toast.makeText(context, "Resultado: $status", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
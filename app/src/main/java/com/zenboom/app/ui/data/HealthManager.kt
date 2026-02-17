package com.zenboom.app.data

import android.content.Context
import kotlinx.coroutines.delay
import kotlin.random.Random

class HealthManager(private val context: Context) {

    // Esta es la función que AnalysisFragment está buscando y no encuentra
    suspend fun fetchLatestVitals(): Pair<Int, Int> {
        delay(1500) // Simula que el sensor está "pensando"

        // Genera datos simulados (BPM entre 60-110, Oxígeno 90-99)
        val simulatedBpm = Random.nextInt(60, 110)
        val simulatedSpo2 = Random.nextInt(90, 99)

        return Pair(simulatedBpm, simulatedSpo2)
    }
}
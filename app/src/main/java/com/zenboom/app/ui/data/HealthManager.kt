package com.zenboom.app.data

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Percentage
import java.time.Instant
import java.time.Duration

class HealthManager(context: Context) {
    private val client = HealthConnectClient.getOrCreate(context)

    suspend fun fetchLatestVitals(): Pair<Int, Int> {
        val timeFilter = TimeRangeFilter.after(Instant.now().minus(Duration.ofMinutes(30)))

        val hrRecords = client.readRecords(ReadRecordsRequest(HeartRateRecord::class, timeFilter))
        val oxRecords = client.readRecords(ReadRecordsRequest(OxygenSaturationRecord::class, timeFilter))

        val bpm = hrRecords.records.lastOrNull()?.samples?.lastOrNull()?.beatsPerMinute?.toInt() ?: 70
        val spo2 = oxRecords.records.lastOrNull()?.percentage?.toInt() ?: 98

        return Pair(bpm, spo2) as Pair<Int, Int>
    }
}

private fun Percentage?.toInt() {
    TODO("Not yet implemented")
}

package com.zenboom.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anxiety_history")
data class AnxietyRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val heartRate: Int,
    val oxygen: Int,
    val stressLevel: String
)
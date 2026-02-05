package com.zenboom.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ZenboomDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ZenboomNativo.db"
        private const val DATABASE_VERSION = 1

        // Definición de la tabla
        const val TABLE_HISTORY = "history"
        const val COLUMN_ID = "id"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_HEART_RATE = "heart_rate"
        const val COLUMN_OXYGEN = "oxygen"
        const val COLUMN_STRESS = "stress_level"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_HISTORY (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TIMESTAMP LONG, " +
                "$COLUMN_HEART_RATE INTEGER, " +
                "$COLUMN_OXYGEN INTEGER, " +
                "$COLUMN_STRESS TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    // --- MÉTODOS CRUD ---

    fun insertRecord(hr: Int, oxygen: Int, stress: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TIMESTAMP, System.currentTimeMillis())
            put(COLUMN_HEART_RATE, hr)
            put(COLUMN_OXYGEN, oxygen)
            put(COLUMN_STRESS, stress)
        }
        db.insert(TABLE_HISTORY, null, values)
        db.close()
    }

    fun getAllRecords(): List<AnxietyRecord> {
        val list = mutableListOf<AnxietyRecord>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_HISTORY ORDER BY $COLUMN_TIMESTAMP DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val record = AnxietyRecord(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)),
                    heartRate = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEART_RATE)),
                    oxygen = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OXYGEN)),
                    stressLevel = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STRESS))
                )
                list.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}
package com.example.slice

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class LocationData(val latitude: Double, val longitude: Double)

class LocationDb(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "location.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "location"
        private const val COLUMN_ID = "id"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_LATITUDE REAL," +
                "$COLUMN_LONGITUDE REAL)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertLocation(latitude: Double, longitude: Double) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
        }
        try {
            db.insert(TABLE_NAME, null, values)
            Log.d("LocationDb", "Inserted location: Latitude: $latitude, Longitude: $longitude")
        } catch (e: Exception) {
            Log.e("LocationDb", "Error inserting location", e)
        } finally {
            db.close()
        }
    }

    fun getLastLocation(): LocationData? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_LATITUDE, COLUMN_LONGITUDE),
            null,
            null,
            null,
            null,
            "$COLUMN_ID DESC",
            "1"
        )
        return try {
            if (cursor.moveToFirst()) {
                val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE))
                val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                Log.d("LocationDb", "Retrieved last location: Latitude: $latitude, Longitude: $longitude>>>>>>>>>>>>>>")
                LocationData(latitude, longitude)
            } else {
                Log.d("LocationDb", "No location data available----->>>>>.")
                null
            }
        } catch (e: Exception) {
            Log.e("LocationDb", "Error fetching last location---------->>>>>>>", e)
            null
        } finally {
            cursor.close()
            db.close()
        }
    }
}

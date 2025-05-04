package com.example.slice

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.os.Build

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.slice"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        requestLocationPermission()

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "getLocation" -> {
                    val db = LocationDb(applicationContext)
                    val location = db.getLastLocation()
                    if (location != null) {
                        result.success(
                            mapOf(
                                "latitude" to location.latitude,
                                "longitude" to location.longitude
                            )
                        )
                    } else {
                        result.error("UNAVAILABLE", "Location not available.", null)
                    }
                }

                "startLocationService" -> {
                    try {
                        if (isLocationPermissionGranted()) {
                            val intent = Intent(applicationContext, LocationService::class.java)
                            applicationContext.startService(intent)
                            Log.d("MainActivity", "LocationService started successfully")
                            result.success("Service is Started")
                        } else {
                            result.error("PERMISSION_DENIED", "Location permission is not granted.", null)
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error starting LocationService: ${e.message}")
                        result.error("SERVICE_ERROR", "Failed to start LocationService", null)
                    }
                }

                else -> result.notImplemented()
            }
        }
    }


    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Location permission granted")
                } else {
                    Log.d("MainActivity", "Location permission denied>>>>>>>>>>>>>")

                }
            }
        }
    }
}

package com.example.slice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.*

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        Log.d("LocationService", "LocationService started >>>>>>>>>>>>>>>>")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {



                    Log.d("LocationService", "Received location: Latitude: ${it.latitude}, Longitude: ${it.longitude}>>>>>>>>>>>")
                    val dbHelper = LocationDb(applicationContext)
                    dbHelper.insertLocation(it.latitude, it.longitude)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onBind(intent: Intent?): IBinder? {



        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)





        Log.d("LocationService", "Location updates stopped>>>>>>>>>>>>>>>>>")
    }
}

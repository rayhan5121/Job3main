package com.example.findmyfriends_firebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.findmyfriends_firebase.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.findmyfriends_firebase.databinding.ActivityMapsBinding
import com.example.findmyfriends_firebase.viewmodel.FirestoreViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var firestoreViewModel: FirestoreViewModel

    private val binding by lazy {
        ActivityMapsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        binding.btnZoomIn.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }
        binding.btnZoomOut.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
        binding.btnhomeOut.setOnClickListener {

            startActivity(Intent(this@MapsActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        firestoreViewModel.getAllUsers(this) {
            for (user in it) {
                val userLocation = user.location
                val latLng = parseLocation(userLocation)
                if (userLocation.isEmpty()||userLocation == "Don't found any location yet"||userLocation == "Location not available") {
                    LatLng(37.4220936, -122.083922)
                }else{
                    val markerOptions = MarkerOptions().position(latLng).title(user.displayName)
                    googleMap.addMarker(markerOptions)
                }
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }

    private fun parseLocation(location: String): LatLng {
        val latLngSplit = location.split(", ")
        val latitude = latLngSplit[0].substringAfter("Lat: ").toDouble()
        val longitude = latLngSplit[1].substringAfter("Long: ").toDouble()
        return LatLng(latitude, longitude)
    }

}
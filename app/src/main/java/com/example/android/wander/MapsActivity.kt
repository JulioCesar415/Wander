package com.example.android.wander

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.android.wander.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
//                SupportMapFragment is a way to get google map in application
            .findFragmentById(R.id.map) as SupportMapFragment
//        .getMapAsync method helps prepare google map
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    onMapReady method is called when map is loaded
    override fun onMapReady(googleMap: GoogleMap) {
//        set map to google map that is passed in
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        create latitude and longitude object
        val sydney = LatLng(-34.0, 151.0)
//        add marker with position at sydney with title Marker in Sydney
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        move camera to sydney
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
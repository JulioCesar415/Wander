package com.example.android.wander


import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.android.wander.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
//    used for logging purposes
    private val TAG = MapsActivity::class.java.simpleName
//    request permission variable
    private val REQUEST_LOCATION_PERMISSION = 1

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
//    method triggered when map is ready to be used and provides a non-null instance
//    of GoogleMap. This method will only be triggered when user has installed Google Play Services

    override fun onMapReady(googleMap: GoogleMap) {
//        set map to google map that is passed in
        map = googleMap
//        value for latitude
        val latitude = 33.922980277661814
//        value for longitude
        val longitude = -118.14075959579965
//        level determines how zoomed in we are on map
        val zoomLevel = 18f
//        variable defines size of overlay
        val overlaySize = 100f
//        object containing longitude and latitude coordinates
        val homeLatLng = LatLng(latitude, longitude)
//        move screen to home by calling moveCamera function on GoogleMap object
//        pass CameraUpdate object which defines camera move using CameraUpdateFactory
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
//        add marker to location
        map.addMarker(MarkerOptions().position(homeLatLng))

        val androidOverlay = GroundOverlayOptions()
//                create bitmap descriptor from bitmap image
            .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
//                add position to ground overlay options
            .position(homeLatLng, overlaySize)

        map.addGroundOverlay(androidOverlay)

        setMapLongClick(map)

        setPoiClick(map)

        setMapStyle(map)

        enableMyLocation()
    }

    //    method initializes contents of Activity's standard options menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        inflater
        val inflater = menuInflater
//        inflate map_options
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    //    this hook is called whenever an item in your options menu is selected
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        change type of map based on user's selection
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

//    method called when user makes a long-press on the map but only if none of the
//    overlays of the map handled the gesture. implementations of method always invoked
//    on Android UI thread
    private fun setMapLongClick(map: GoogleMap){
        map.setOnMapLongClickListener { latLng ->
//            additional text displayed below title
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
//                        set title of marker to dropped_pin
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

//    listener for POI taps.
    private fun setPoiClick(map: GoogleMap){
//        add onPoiClickListener on the passed in GoogleMap
        map.setOnPoiClickListener { poi ->
//            place marker in poi location
            val poiMarker = map.addMarker(
                MarkerOptions()
//                        set position to latLng of poi
                    .position(poi.latLng)
//                        set title to name of poi
                    .title(poi.name)
            )
//            call showInfoWindow method to immediately show info window
            poiMarker?.showInfoWindow()
        }
    }

    private fun setMapStyle(map: GoogleMap){
        try {
//            customize the styling of the base map using JSON object defined
//            in a raw resource file
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
//                    load JSON file we created on googleMap
                    R.raw.map_style
                )
            )
            if (!success){
                Log.e(TAG, "Style parsing failed")
            }
        }
//        if style isnt found throw exception
        catch (e: Resources.NotFoundException){
            Log.e(TAG, "cant find style. Error: ", e)
        }
    }
//    method checks that user has granted permission for location. will return true or false
    private fun isPermissionGranted(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }

//    method enables location if permission is granted
    private fun enableMyLocation(){
        if (isPermissionGranted()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.setMyLocationEnabled(true)
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION){
            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                enableMyLocation()
            }
        }
    }
}
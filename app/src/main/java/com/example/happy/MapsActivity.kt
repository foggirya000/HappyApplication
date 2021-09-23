package com.example.happy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.happy.databinding.ActivityMapsBinding
import com.google.android.gms.location.LocationServices


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var location: Location
    var currLat: Double = 0.0
    var currLng: Double = 0.0
    var tmpLat = 0.0
    var tmpLong = 0.0


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
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
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocation()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getLocation() {
        val client = LocationServices.getFusedLocationProviderClient(this)
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
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return
        }
        client.lastLocation.addOnSuccessListener {
            // lambda is called when location is available
            if (it != null) {

                if(intent.extras != null) {

                    tmpLat = intent.extras?.getDouble("LAT")!!
                    tmpLong = intent.extras?.getDouble("LONG")!!
                }

                if( tmpLat != 0.0 && tmpLong != 0.0) {
                    currLat = tmpLat!!
                    currLng = tmpLong!!
                }
                else {
                    currLat = it.latitude
                    currLng = it.longitude
                }
                mMap.addMarker(MarkerOptions().position(LatLng(currLat, currLng)).title("MY LOCATION"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currLat, currLng), 15f))
            }
        }
    }

    // override finish() to return the complete boolean back to the MainActivity
    override fun finish() {

        var intent = Intent(this, NewMoodFragment::class.java)
        with(intent) {
            putExtra("LAT", currLat)
            putExtra("LNG", currLng)
        }
        setResult(RESULT_OK, intent)
        super.finish()
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            // Got permission from user
            getLocation()
        }
    }
}
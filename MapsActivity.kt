package com.sehatin.ittp

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sehatin.ittp.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var myData: mydataku? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myData = parcelable(DetailActivity.EXTRA_MY_DATA)

        supportActionBar?.apply {
            title = myData?.name.toString()
            setDisplayHomeAsUpEnabled(true)
        }

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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val lat: Double? = myData?.lat
        val lang: Double? = myData?.lang
        if (lat != null && lang != null) {
            val location = LatLng(lat, lang)
            mMap.addMarker(MarkerOptions().position(location).title(myData?.name.toString()))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f))
        }
    }

    private inline fun <reified T : Parcelable> Activity.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") intent.getParcelableExtra(key) as? T
    }
    companion object {
        const val EXTRA_MY_DATA = "extra_my_data"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }}
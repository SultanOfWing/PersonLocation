package seleznov.nope.personlocation.view

import android.Manifest
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import seleznov.nope.personlocation.R
import seleznov.nope.personlocation.helper.PermissionInspector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.CameraUpdate
import android.text.method.TextKeyListener.clear
import com.google.android.gms.maps.model.MarkerOptions





/**
 * Created by User on 09.07.2018.
 */

class MapsFragment : SupportMapFragment() {

    private val LOCATION_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    private val REQUEST_LOCATION_PERMISSIONS = 0

    private lateinit var googleMap: GoogleMap

    private var googleApi = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
    private var locationRequest = LocationRequest.create()
    private var provider = LocationServices.FusedLocationApi
    private var locationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            //TODO
        }
    }

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(map : GoogleMap) {
                googleMap = map
            }
        })
    }

    override fun onStart() {
        super.onStart()
        googleApi.connect()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        if (PermissionInspector.checkPermission(context, LOCATION_PERMISSIONS))
            provider.requestLocationUpdates(googleApi, locationRequest,
                    locationListener)
        else
            requestPermissions(LOCATION_PERMISSIONS,
                    REQUEST_LOCATION_PERMISSIONS)

    }

    override fun onStop() {
        super.onStop()
        googleApi.disconnect()
        provider.removeLocationUpdates(googleApi, locationListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSIONS -> {
                if (PermissionInspector.checkPermission(context, LOCATION_PERMISSIONS)) {
                    provider.requestLocationUpdates(googleApi, locationRequest,
                            locationListener)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun showPersonOnMap(lat: Double, lng: Double){
        val latLng = LatLng(lat, lng)
        val bounds = LatLngBounds.Builder()
                .include(latLng).build()
        val margin = resources.getDimensionPixelSize(R.dimen.map_margin)
        val update = CameraUpdateFactory.newLatLngBounds(bounds, margin)
        googleMap.animateCamera(update)

        val myMarker = MarkerOptions()
                .position(latLng)
        googleMap.clear()
        googleMap.addMarker(myMarker)
    }
}
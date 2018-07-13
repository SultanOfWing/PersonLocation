package seleznov.nope.personlocation.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat.requestPermissions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

/**
 * Created by User on 13.07.2018.
 */
class LocationManager(val context : Activity, onLocationChanged: OnLocationChanged)
    : GoogleApiClient.ConnectionCallbacks{

    private val LOCATION_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)


    private lateinit var googleApi: GoogleApiClient

    private val locationRequest = LocationRequest.create()
    private val provider = LocationServices.FusedLocationApi


    val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val lat = location.latitude
            val lon = location.longitude
            onLocationChanged.locationChanged(lat, lon)

        }
    }

    override fun onConnected(p0: Bundle?) {
        if (PermissionInspector.checkPermission(context, LOCATION_PERMISSIONS)){
            requestUpdates()
        }
        else
            requestPermissions(context, LOCATION_PERMISSIONS,
                     Constants.REQUEST_LOCATION_PERMISSIONS)
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented")
        //To change body of created functions use File | Settings | File Templates.
    }

    fun connect() {
        googleApi = GoogleApiClient.Builder(context.applicationContext)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build()
        googleApi.connect()
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
    }

    @SuppressLint("MissingPermission")
    fun requestUpdates(){
        provider.requestLocationUpdates(googleApi, locationRequest,
                locationListener)
    }

    fun disconnect() {
        if(googleApi.isConnected){
            provider.removeLocationUpdates(googleApi, locationListener)
        }
        googleApi.disconnect()
    }

    interface OnLocationChanged {
        fun locationChanged(lat: Double, lon: Double)
    }
}
package seleznov.nope.personlocation.view

import android.Manifest
import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import seleznov.nope.personlocation.helper.PermissionInspector

/**
 * Created by User on 09.07.2018.
 */

class MapsFragment : SupportMapFragment() {

    private val LOCATION_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    private val REQUEST_LOCATION_PERMISSIONS = 0

    private var googleApi = GoogleApiClient.Builder(activity)
            .addApi(LocationServices.API).build()
    private var locationRequest = LocationRequest.create()
    private var provider = LocationServices.FusedLocationApi
    private var locationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            //TODO
        }

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
}
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
import seleznov.nope.personlocation.R
import seleznov.nope.personlocation.helper.PermissionInspector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.disposables.Disposable
import seleznov.nope.personlocation.helper.EventBus
import seleznov.nope.personlocation.model.Person


/**
 * Created by User on 09.07.2018.
 */

class MapsFragment : SupportMapFragment(), GoogleApiClient.ConnectionCallbacks {

    private val LOCATION_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    private val REQUEST_LOCATION_PERMISSIONS = 0
    private val ZOOM = 12.0f

    private lateinit var googleMap: GoogleMap
    private lateinit var googleApi: GoogleApiClient
    private lateinit var disposable : Disposable

    private var locationRequest = LocationRequest.create()
    private var provider = LocationServices.FusedLocationApi
    private var locationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            //TODO
        }
    }

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        googleApi=  GoogleApiClient.Builder(activity.applicationContext)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build()
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

        disposable = EventBus.instance.subscribe({ message-> if (message is Person)
        {
            val person = message
            showPersonOnMap(person.lat, person.lon)
        } })

    }

    override fun onStop() {
        super.onStop()
        if(googleApi.isConnected){
            provider.removeLocationUpdates(googleApi, locationListener)
        }
        disposable.dispose()
        googleApi.disconnect()
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

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        if (PermissionInspector.checkPermission(context, LOCATION_PERMISSIONS))
            provider.requestLocationUpdates(googleApi, locationRequest,
                    locationListener)
        else
            requestPermissions(LOCATION_PERMISSIONS,
                    REQUEST_LOCATION_PERMISSIONS)
    }

    fun showPersonOnMap(lat: Double, lng: Double){
        val latLng = LatLng(lat, lng)
        val update = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM)
        googleMap.animateCamera(update)

        val myMarker = MarkerOptions()
                .position(latLng)
        googleMap.clear()
        googleMap.addMarker(myMarker)
    }
}
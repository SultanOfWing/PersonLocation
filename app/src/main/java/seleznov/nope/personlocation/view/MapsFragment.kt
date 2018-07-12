package seleznov.nope.personlocation.view


import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.disposables.Disposable
import seleznov.nope.personlocation.helper.EventBus
import seleznov.nope.personlocation.model.Person


/**
 * Created by User on 09.07.2018.
 */

class MapsFragment : SupportMapFragment() {

    private val ZOOM = 12.0f

    private lateinit var googleMap: GoogleMap
    private lateinit var disposable : Disposable


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
        disposable = EventBus.instance.subscribe({ message-> if (message is Person)
        {
            val person = message
            showPersonOnMap(person.lat, person.lon)
        } })

    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()

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
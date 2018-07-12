package seleznov.nope.personlocation.view

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import seleznov.nope.personlocation.R
import seleznov.nope.personlocation.databinding.FragmentPersonListBinding
import seleznov.nope.personlocation.helper.EventBus
import seleznov.nope.personlocation.helper.PermissionInspector
import seleznov.nope.personlocation.model.Person
import seleznov.nope.personlocation.view.adapter.PersonAdapter
import seleznov.nope.personlocation.viewmodel.PersonListViewModel

/**
 * Created by User on 09.07.2018.
 */
class PersonListFragment : Fragment(), PersonAdapter.OnItemClickListener, GoogleApiClient.ConnectionCallbacks{

    private val LOCATION_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    private val REQUEST_LOCATION_PERMISSIONS = 0

    private lateinit var binding: FragmentPersonListBinding
    private lateinit var locationListener: LocationListener
    private lateinit var googleApi: GoogleApiClient

    private val personAdapter = PersonAdapter(arrayListOf(), this)
    private var locationRequest = LocationRequest.create()
    private var provider = LocationServices.FusedLocationApi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleApi=  GoogleApiClient.Builder(activity.applicationContext)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_person_list,container , false)

        val viewModel = ViewModelProviders.of(this).get(PersonListViewModel::class.java)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = personAdapter
        viewModel.list.observe(this,
                Observer<List<Person>> { it?.let{ personAdapter.setList(it as ArrayList<Person>)} })

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                val lat = location?.latitude
                val lon = location?.longitude
                viewModel.updateCurrentPosition(lat, lon)
            }
        }

        return binding.root;
    }

    override fun onStart() {
        super.onStart()
        googleApi.connect()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
    }

    override fun onStop() {
        super.onStop()
        if(googleApi.isConnected){
            provider.removeLocationUpdates(googleApi, locationListener)
        }
        googleApi.disconnect()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewModel?.list?.value = null
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

    override fun onConnected(p0: Bundle?) {
        if (PermissionInspector.checkPermission(context, LOCATION_PERMISSIONS))
            provider.requestLocationUpdates(googleApi, locationRequest,
                    locationListener)
        else
            requestPermissions(LOCATION_PERMISSIONS,
                    REQUEST_LOCATION_PERMISSIONS)
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClick(position: Int) {
        val person = binding.viewModel?.list?.value?.get(position)
        EventBus.instance.publish(person!!)
    }


}
package seleznov.nope.personlocation.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import seleznov.nope.personlocation.helper.LocationManager
import seleznov.nope.personlocation.R
import seleznov.nope.personlocation.databinding.FragmentPersonListBinding
import seleznov.nope.personlocation.helper.Constants
import seleznov.nope.personlocation.helper.EventBus
import seleznov.nope.personlocation.model.Person
import seleznov.nope.personlocation.view.adapter.PersonAdapter
import seleznov.nope.personlocation.viewmodel.PersonListViewModel

/**
 * Created by User on 09.07.2018.
 */
class PersonListFragment : Fragment(), PersonAdapter.OnItemClickListener, LocationManager.OnLocationChanged {

    private lateinit var binding: FragmentPersonListBinding
    private lateinit var locationManager: LocationManager

    private val personAdapter = PersonAdapter(arrayListOf(), this)



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_person_list,container , false)

        val viewModel = ViewModelProviders.of(this).get(PersonListViewModel::class.java)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = personAdapter

        viewModel.liveData.observe(this,
                Observer<LinkedHashMap<String?, Person>>
                { it?.let{ personAdapter.setList(ArrayList(
                        it.values))} })

        return binding.root;
    }

    override fun onStart() {
        super.onStart()
        locationManager = LocationManager(context, this)
        locationManager.connect()

    }

    override fun onStop() {
        super.onStop()
        locationManager.disconnect()

    }

    override fun locationChanged(lat: Double, lon: Double) {
        binding.viewModel?.updateCurrentPosition(lat, lon)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.REQUEST_LOCATION_PERMISSIONS -> {
                locationManager.requestUpdates()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onItemClick(position: Int) {
        val col =  binding.viewModel?.liveData?.value?.values as Collection<Person>
        val list = ArrayList(col)
        val person = list[position]
        EventBus.instance.publish(person)
    }


}
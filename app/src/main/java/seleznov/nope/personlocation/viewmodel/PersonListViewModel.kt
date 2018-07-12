package seleznov.nope.personlocation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import seleznov.nope.personlocation.helper.EventBus
import seleznov.nope.personlocation.model.Person
import seleznov.nope.personlocation.model.PersonRepository

/**
 * Created by User on 09.07.2018.
 */

class PersonListViewModel : ViewModel(), PersonRepository.OnDataReadyCallback {

    private var personRepository = PersonRepository(this)
    val lat = ObservableField<String>()
    val lon = ObservableField<String>()
    var list = MutableLiveData<List<Person>>()

    override fun onPersonDataReady(data: List<Person>) {
        list.value = data
    }
    override fun onMyDataReady(data: Person) {
        lat.set(data.lat.toString())
        lon.set(data.lon.toString())
    }

    fun showCurrentPosition(){
        val person = Person()
        person.lat = lat.get().toDouble()
        person.lon = lon.get().toDouble()
        EventBus.instance.publish(person)
    }

    fun updateCurrentPosition(lat: Double?, lon: Double?){
        personRepository.putData(lat, lon)
    }
}

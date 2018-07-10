package seleznov.nope.personlocation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import seleznov.nope.personlocation.model.Person
import seleznov.nope.personlocation.model.PersonRepository

/**
 * Created by User on 09.07.2018.
 */

class PersonListViewModel : ViewModel(), PersonRepository.OnDataReadyCallback {

    internal var mPersonRepository = PersonRepository(this)
    var list = MutableLiveData<List<Person>>()

    override fun onDataReady(data: List<Person>) {
        list.value = data
    }
}

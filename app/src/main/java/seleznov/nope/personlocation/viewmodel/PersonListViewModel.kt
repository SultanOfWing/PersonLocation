package seleznov.nope.personlocation.viewmodel

import android.arch.lifecycle.ViewModel
import seleznov.nope.personlocation.model.Person
import seleznov.nope.personlocation.model.PersonRepository

/**
 * Created by User on 09.07.2018.
 */

class PersonListViewModel : ViewModel() {
    internal var mPersonRepository = PersonRepository()
    var mPersonList: List<Person>? = null

    val onDataReadyCallback = object : PersonRepository.OnDataReadyCallback {
        override fun onDataReady(data: List<Person>) {
            mPersonList = data;
        }
    }

    fun getPersonList():List<Person>?{
        return mPersonList;
    }

    fun getPerson(pos:Int):Person{
        return mPersonList!!.get(pos);
    }


}

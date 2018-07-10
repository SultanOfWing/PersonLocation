package seleznov.nope.personlocation.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * Created by User on 09.07.2018.
 */
private const val TAG = "PersonRepository";

class PersonRepository(onDataReadyCallback: OnDataReadyCallback) {


    private val personList = ArrayList<Person>()
    private val firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    init {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.child("person").children) {
                    val person = ds.getValue(Person::class.java)
                    Log.i(TAG, person.toString())
                    personList.add(person!!)
                }
                onDataReadyCallback.onDataReady(personList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, error.message)
            }
        })
    }

    interface OnDataReadyCallback {
        fun onDataReady(data: List<Person>)
    }

}

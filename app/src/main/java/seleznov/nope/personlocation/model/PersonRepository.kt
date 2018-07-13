package seleznov.nope.personlocation.model

import android.util.Log
import com.google.firebase.database.*


/**
 * Created by User on 09.07.2018.
 */
private const val TAG = "PersonRepository";

class PersonRepository(onDataReadyCallback: OnDataReadyCallback) {

    private val personMap = LinkedHashMap<String?, Person>()
    private val firebaseRef: DatabaseReference = FirebaseDatabase
            .getInstance().reference

    init {
        firebaseRef.child("me").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                val me = p0?.getValue(Person::class.java)
                onDataReadyCallback.onMyDataReady(me!!)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, error.message)
            }
        })

        firebaseRef.child("person").addChildEventListener(object : ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val person = p0.getValue(Person::class.java)
                personMap.put(p1, person!!)
                onDataReadyCallback.onPersonDataReady(personMap)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val person = p0?.getValue(Person::class.java)
                personMap.put(p1, person!!)
                onDataReadyCallback.onPersonDataReady(personMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, error.message)
            }
        })

    }

    fun putData(lat: Double, lon: Double){
        val rootchild = firebaseRef.child("me")
        rootchild.child("lat").setValue(lat)
        rootchild.child("lon").setValue(lon)
    }

    interface OnDataReadyCallback {
        fun onPersonDataReady(map : LinkedHashMap<String?, Person>)
        fun onMyDataReady(data : Person)
    }

}

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

class PersonRepository(onDataReadyCallback: OnDataReadyCallback) {

    private val TAG = "PersonRepository";
    private val mPersonList = ArrayList<Person>()
    private val mFirebaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    init {
        mFirebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.child("person").children) {
                    val person = ds.getValue(Person::class.java)
                    Log.i(TAG, person.toString())
                    mPersonList.add(person!!)
                }
                onDataReadyCallback.onDataReady(mPersonList)
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

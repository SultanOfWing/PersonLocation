package seleznov.nope.personlocation.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * Created by User on 09.07.2018.
 */

class PersonRepository {

    private val mPersonList = ArrayList<Person>()
    private val mFirebaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mOnDataReadyCallback: OnDataReadyCallback? = null

    init {
        mFirebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val person = ds.getValue(Person::class.java)
                    mPersonList.add(person!!)
                }
                mOnDataReadyCallback!!.onDataReady(mPersonList)

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    interface OnDataReadyCallback {
        fun onDataReady(data: List<Person>)
    }


}

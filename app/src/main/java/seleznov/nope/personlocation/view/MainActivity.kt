package seleznov.nope.personlocation.view

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.SupportMapFragment
import seleznov.nope.personlocation.R

const val REQUEST_ERROR: Int = 0

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = supportFragmentManager.findFragmentById(R.id.list_container)
        if(fragment == null){
            supportFragmentManager.beginTransaction()
                    .replace(R.id.list_container, PersonListFragment())
                    .replace(R.id.map_container, MapsFragment())
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val apiAvailability = GoogleApiAvailability.getInstance()
        val errorCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (errorCode != ConnectionResult.SUCCESS)
        {
            val errorDialog = apiAvailability
                    .getErrorDialog(this, errorCode, REQUEST_ERROR,
                            object: DialogInterface.OnCancelListener{
                                override fun onCancel(dialog: DialogInterface?) {
                                    finish()
                                }
                            })
            errorDialog.show()
        }
    }
}

package seleznov.nope.personlocation.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import seleznov.nope.personlocation.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.list_container,
                PersonListFragment()).commit()
    }

}

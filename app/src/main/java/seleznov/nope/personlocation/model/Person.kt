package seleznov.nope.personlocation.model

import android.databinding.BaseObservable
import android.databinding.Bindable

import com.google.firebase.database.IgnoreExtraProperties

import seleznov.nope.personlocation.BR


/**
 * Created by User on 09.07.2018.
 */
@IgnoreExtraProperties
class Person internal constructor() : BaseObservable() {
    @get:Bindable
    var name: String? = null
        set(name) {
            field = name
            notifyPropertyChanged(BR.name)
        }
    @get:Bindable
    var email: String? = null
        set(email) {
            field = email
            notifyPropertyChanged(BR.email)
        }
    @get:Bindable
    var lon: Int = 0
        set(lon) {
            field = lon
            notifyPropertyChanged(BR.lon)
        }
    @get:Bindable
    var lat: Int = 0
        set(lat) {
            field = lat
            notifyPropertyChanged(BR.lat)
        }
}

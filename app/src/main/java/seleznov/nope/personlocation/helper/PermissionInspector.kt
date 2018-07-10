package seleznov.nope.personlocation.helper

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * Created by User on 22.05.2018.
 */

object PermissionInspector {

    fun checkPermission(context: Context, permissions: Array<String>): Boolean {
        val result = ContextCompat.checkSelfPermission(context,
                permissions[0])

        return result == PackageManager.PERMISSION_GRANTED
    }

}

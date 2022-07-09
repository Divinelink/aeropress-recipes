package aeropresscipe.divinelink.aeropress.base

import aeropresscipe.divinelink.aeropress.helpers.SharedPreferences
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

val preferences: SharedPreferences by lazy {
    BaseApplication.sharedPreferences
}

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
            private set
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        sharedPreferences = SharedPreferences(this)
    }


}

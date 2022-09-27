package aeropresscipe.divinelink.aeropress.base

import android.content.Context

class AppInitialization {

    fun onFirstEverAppLaunch(context: Context) {
        Store.onFirstEverAppLaunch()
    }
}

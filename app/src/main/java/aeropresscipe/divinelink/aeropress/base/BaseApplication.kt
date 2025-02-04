package aeropresscipe.divinelink.aeropress.base

import android.app.Application
import com.divinelink.aeropress.recipes.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {

  companion object {
    lateinit var instance: BaseApplication
      private set
  }

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    instance = this
  }
}

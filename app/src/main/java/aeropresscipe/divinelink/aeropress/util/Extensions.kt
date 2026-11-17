package aeropresscipe.divinelink.aeropress.util

import aeropresscipe.divinelink.aeropress.model.Theme
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

fun updateForTheme(theme: Theme) = when (theme) {
  Theme.SYSTEM -> {
    Timber.d("Setting to follow system")
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
  }
  Theme.LIGHT -> {
    Timber.d("Setting to always night")
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
  }
  Theme.DARK -> {
    Timber.d("Setting to always night")
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
  }
}

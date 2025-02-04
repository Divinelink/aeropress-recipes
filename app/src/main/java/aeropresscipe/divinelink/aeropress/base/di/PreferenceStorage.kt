package aeropresscipe.divinelink.aeropress.base.di

import aeropresscipe.divinelink.aeropress.base.di.DataStorePreferenceStorage.PreferencesKeys.PREF_SELECTED_THEME
import aeropresscipe.divinelink.aeropress.base.di.DataStorePreferenceStorage.PreferencesKeys.PREF_TIMER_SOUND
import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// @Singleton
// class Preferences @Inject constructor(var preferences: SharedPreferences) {
//    var muteSound: Boolean
//        get() = preferences.getBoolean(MUTE_SOUND, false)
//        set(value) = preferences.edit().putBoolean(MUTE_SOUND, value).apply()
//
//    // Settings
//    var theme: String
//        get() = preferences.getString(SettingsValues.THEME, if (DynamicTheme.systemThemeAvailable()) "system" else "light") ?: "light"
//        set(value) = preferences.edit().putString(SettingsValues.THEME, value).apply()
//
//    companion object {
//        private const val MUTE_SOUND = "MUTE_SOUND"
//    }
// }

interface PreferenceStorage {
  suspend fun selectTheme(theme: String)
  val selectedTheme: Flow<String>

  suspend fun updateTimerSound(on: Boolean)
  val timerSound: Flow<Boolean>
}

@Singleton
class DataStorePreferenceStorage @Inject constructor(
  private val dataStore: DataStore<Preferences>,
) : PreferenceStorage {
  companion object {
    const val PREFS_NAME = "aeropress_recipes"
  }

  object PreferencesKeys {
    val PREF_SELECTED_THEME = stringPreferencesKey("settings.theme")
    val PREF_TIMER_SOUND = booleanPreferencesKey("settings.timer.sound")
  }

  override suspend fun selectTheme(theme: String) {
    dataStore.edit {
      Timber.d("Set theme to $theme")
      it[PREF_SELECTED_THEME] = theme
    }
  }

  override val selectedTheme =
    dataStore.data.map {
      it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey
    }

  override suspend fun updateTimerSound(on: Boolean) {
    dataStore.edit {
      it[PREF_TIMER_SOUND] = on
    }
  }

  override val timerSound =
    dataStore.data.map {
      it[PREF_TIMER_SOUND] ?: true
    }
}

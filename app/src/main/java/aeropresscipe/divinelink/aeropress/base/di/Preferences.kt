package aeropresscipe.divinelink.aeropress.base.di

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(var preferences: SharedPreferences) {
    var muteSound: Boolean
        get() = preferences.getBoolean(MUTE_SOUND, false)
        set(value) = preferences.edit().putBoolean(MUTE_SOUND, value).apply()

    companion object {
        private const val MUTE_SOUND = "MUTE_SOUND"
    }
}

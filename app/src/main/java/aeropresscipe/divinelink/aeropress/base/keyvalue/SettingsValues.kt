package aeropresscipe.divinelink.aeropress.base.keyvalue

import aeropresscipe.divinelink.aeropress.base.BaseApplication
import aeropresscipe.divinelink.aeropress.base.di.PreferencesEntryPoint
import dagger.hilt.EntryPoints

class SettingsValues : StoreValues {
    private val entryPoint = EntryPoints.get(BaseApplication.instance.applicationContext, PreferencesEntryPoint::class.java)
    private val preferences = entryPoint.getPreferences()

    override fun onFirstEverAppLaunch() {
        // nothing yet
    }

    override fun getKeysToIncludeInBackup(): List<String> {
        return listOf()
    }

    companion object {
        const val THEME = "settings.theme"
    }

    var theme: Theme // todo inject preference logic on StoreValues()
        get() = Theme.valueOf(preferences.theme.uppercase())
        set(theme) {
            preferences.theme = theme.value
        }

    enum class Theme(val value: String) {
        SYSTEM("system"),
        LIGHT("light"),
        DARK("dark");
    }
}

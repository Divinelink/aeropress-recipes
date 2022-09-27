package aeropresscipe.divinelink.aeropress.util

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.Store
import aeropresscipe.divinelink.aeropress.base.keyvalue.SettingsValues.Theme
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

open class DynamicTheme {

    private var onCreateNightModeConfiguration = 0

    fun onCreate(activity: Activity) {
        val previousGlobalConfiguration = globalNightModeConfiguration
        onCreateNightModeConfiguration = ConfigurationUtil.getNightModeConfiguration(activity)
        globalNightModeConfiguration = onCreateNightModeConfiguration
        activity.setTheme(theme)
        if (previousGlobalConfiguration != globalNightModeConfiguration) {
            Timber.d("Previous night mode has changed previous: " + previousGlobalConfiguration + " now: " + globalNightModeConfiguration)
        }
    }

    fun onResume(activity: Activity) {
        if (onCreateNightModeConfiguration != ConfigurationUtil.getNightModeConfiguration(activity)) {
            Timber.d("Create configuration different from current previous: " + onCreateNightModeConfiguration + " now: " + ConfigurationUtil.getNightModeConfiguration(activity))
        }
    }

    @get:StyleRes
    protected open val theme: Int
        protected get() = R.style.Base_Theme_Aeropress

    companion object {
        private var globalNightModeConfiguration = 0

        fun systemThemeAvailable(): Boolean {
            return Build.VERSION.SDK_INT >= 29
        }

        fun setDefaultDayNightMode(context: Context) {
            val theme = Store.settings().theme
            if (theme === Theme.SYSTEM) {
//            Timber.d("Setting to follow system expecting: " + ConfigurationUtil.getNightModeConfiguration(context.getApplicationContext()));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else if (isDarkTheme(context)) {
                Timber.d("Setting to always night")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                Timber.d("Setting to always day")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

//        CachedInflater.from(context).clear();
        }

        /**
         * Takes the system theme into account.
         */
        fun isDarkTheme(context: Context): Boolean {
            val theme = Store.settings().theme
            return if (theme == Theme.SYSTEM && systemThemeAvailable()) {
                isSystemInDarkTheme(context)
            } else {
                theme == Theme.DARK
            }
        }

        private fun isSystemInDarkTheme(context: Context): Boolean {
            return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }
}

internal object ConfigurationUtil {
    fun getNightModeConfiguration(context: Context): Int {
        return getNightModeConfiguration(context.resources.configuration)
    }

    fun getNightModeConfiguration(configuration: Configuration): Int {
        return configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    }

    fun getFontScale(configuration: Configuration): Float {
        return configuration.fontScale
    }
}

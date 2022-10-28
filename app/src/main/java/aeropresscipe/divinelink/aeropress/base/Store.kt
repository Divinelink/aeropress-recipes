package aeropresscipe.divinelink.aeropress.base

import aeropresscipe.divinelink.aeropress.base.keyvalue.SettingsValues
import androidx.annotation.VisibleForTesting

class Store {

    val settingsValues: SettingsValues = SettingsValues()

    companion object {
        @Volatile
        private var instance: Store? = null
        private fun getInstance(): Store {
            if (instance == null) {
                synchronized(Store::class.java) {
                    if (instance == null) {
                        instance = Store()
                    }
                }
            }
            return instance!!
        }

        fun onFirstEverAppLaunch() {
            settings().onFirstEverAppLaunch()
        }

        val keysToIncludeInBackup: List<String>
            get() {
                val keys: MutableList<String> = ArrayList()
                keys.addAll(settings().getKeysToIncludeInBackup())
                return keys
            }

        /**
         * Forces the store to re-fetch all of it's data from the database.
         * Should only be used for testing!
         */
        @VisibleForTesting
        fun resetCache() {
//            getInstance().store.resetCache()
        }

        /**
         * Restoring a backup changes the underlying disk values, so the cache needs to be reset.
         */
        fun onPostBackupRestore() {
//            getInstance().store.resetCache()
        }

        fun settings(): SettingsValues {
            return getInstance().settingsValues
        }
    }
}

package aeropresscipe.divinelink.aeropress.base.keyvalue

abstract class StoreValues {

    abstract fun onFirstEverAppLaunch()

    abstract fun getKeysToIncludeInBackup(): List<String>
}

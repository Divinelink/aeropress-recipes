package aeropresscipe.divinelink.aeropress.base.keyvalue

interface StoreValues {
  fun onFirstEverAppLaunch()

  fun getKeysToIncludeInBackup(): List<String>
}

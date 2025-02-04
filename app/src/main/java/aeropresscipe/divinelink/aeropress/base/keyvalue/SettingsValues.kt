package aeropresscipe.divinelink.aeropress.base.keyvalue

@Deprecated("Use DataStore instead.")
class SettingsValues : StoreValues {
//    private val entryPoint = EntryPoints.get(BaseApplication.instance.applicationContext, PreferencesEntryPoint::class.java)
//    private val preferences = entryPoint.getPreferences()

  override fun onFirstEverAppLaunch() {
    // nothing yet
  }

  override fun getKeysToIncludeInBackup(): List<String> {
    return listOf()
  }
}

enum class Theme(val storageKey: String) {
  SYSTEM("system"),
  LIGHT("light"),
  DARK("dark")
}

/**
 * Returns the matching [Theme] for the given [storageKey] value.
 */
fun themeFromStorageKey(storageKey: String): Theme? {
  return Theme.values().firstOrNull { it.storageKey == storageKey }
}

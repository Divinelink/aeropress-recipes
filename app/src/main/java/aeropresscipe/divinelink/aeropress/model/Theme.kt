package aeropresscipe.divinelink.aeropress.model

enum class Theme(val storageKey: String) {
  SYSTEM("system"),
  LIGHT("light"),
  DARK("dark"),
}

/**
 * Returns the matching [Theme] for the given [storageKey] value.
 */
fun themeFromStorageKey(storageKey: String): Theme? = Theme
  .entries
  .firstOrNull { it.storageKey == storageKey }

package aeropresscipe.divinelink.aeropress.settings.adapter

data class SettingsItem(
    val title: CharSequence,
    val category: SettingsCategory,
    val isChecked: Boolean
)

enum class SettingsCategory(
    val title: CharSequence
) {

    ABOUT("About")
}

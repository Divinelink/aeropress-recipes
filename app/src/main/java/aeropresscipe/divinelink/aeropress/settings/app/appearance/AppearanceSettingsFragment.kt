package aeropresscipe.divinelink.aeropress.settings.app.appearance

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.settings.DSLConfiguration
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsAdapter
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.configure

class AppearanceSettingsFragment : DSLSettingsFragment(R.string.preferences__appearance) {

    private val themeLabels by lazy { resources.getStringArray(R.array.pref_theme_entries) }
    private val themeValues by lazy { resources.getStringArray(R.array.pref_theme_values) }

    override fun bindAdapter(adapter: DSLSettingsAdapter) {
        adapter.submitList(getConfiguration().toMappingModelList())
    }

    private fun getConfiguration(): DSLConfiguration {
        return configure {
            radioListPref(
                title = DSLSettingsText.from(R.string.preferences__theme),
                listItems = themeLabels,
                selected = 0,
//                selected = themeValues.indexOf(state.theme.serialize()),
                onSelected = {
//                    viewModel.setTheme(activity, SettingsValues.Theme.deserialize(themeValues[it]))
                }
            )
        }
    }
}
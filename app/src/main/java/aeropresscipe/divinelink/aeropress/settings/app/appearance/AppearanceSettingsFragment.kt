package aeropresscipe.divinelink.aeropress.settings.app.appearance

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.keyvalue.SettingsValues.Theme
import aeropresscipe.divinelink.aeropress.settings.DSLConfiguration
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsAdapter
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.configure
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class AppearanceSettingsFragment : DSLSettingsFragment(R.string.preferences__appearance) {
    private lateinit var viewModel: AppearanceSettingsViewModel

    private val themeLabels by lazy { resources.getStringArray(R.array.pref_theme_entries) }
    private val themeValues by lazy { resources.getStringArray(R.array.pref_theme_values) }

    override fun bindAdapter(adapter: DSLSettingsAdapter) {
        viewModel = ViewModelProvider(this)[AppearanceSettingsViewModel::class.java]
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> adapter.submitList(getConfiguration(state).toMappingModelList()) }
            }
        }
    }

    private fun getConfiguration(state: UpdateSettingsState): DSLConfiguration {
        return configure {
            radioListPref(
                title = DSLSettingsText.from(R.string.preferences__theme),
                listItems = themeLabels,
                selected = themeValues.indexOf(state.theme.value),
                onSelected = {
                    viewModel.updateTheme(Theme.valueOf(themeValues[it].uppercase()))
                }
            )
        }
    }
}

package aeropresscipe.divinelink.aeropress.settings.app.appearance

import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import aeropresscipe.divinelink.aeropress.settings.DSLConfiguration
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsAdapter
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.configure
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.divinelink.aeropress.recipes.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppearanceSettingsFragment : DSLSettingsFragment(R.string.preferences__appearance) {
    private val viewModel: AppearanceSettingsViewModel by viewModels()

    private val themeLabels by lazy { resources.getStringArray(R.array.pref_theme_entries) }

    private lateinit var themeValues: List<Theme>

    override fun bindAdapter(adapter: DSLSettingsAdapter) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.availableThemes.collect { themeValues = it }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { adapter.submitList(getConfiguration(it).toMappingModelList()) }
            }
        }
    }

    private fun getConfiguration(state: UpdateSettingsState): DSLConfiguration {
        return configure {
            radioListPref(
                title = DSLSettingsText.from(R.string.preferences__theme),
                listItems = themeLabels,
                selected = themeValues.indexOf(state.theme),
                onSelected = {
                    viewModel.setTheme(themeValues[it])
                }
            )
        }
    }
}

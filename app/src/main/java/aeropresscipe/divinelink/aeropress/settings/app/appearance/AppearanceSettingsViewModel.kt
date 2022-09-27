package aeropresscipe.divinelink.aeropress.settings.app.appearance

import aeropresscipe.divinelink.aeropress.base.Store
import aeropresscipe.divinelink.aeropress.base.keyvalue.SettingsValues
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AppearanceSettingsViewModel : ViewModel(), AppearanceSettingsIntents {

    private val _uiState = MutableStateFlow(UpdateSettingsState(Store.settings().theme))
    val uiState: StateFlow<UpdateSettingsState> = _uiState

    override fun updateTheme(theme: SettingsValues.Theme) {
        Store.settings().theme = theme
        _uiState.update { it.copy(theme = theme) }
    }
}

interface AppearanceSettingsIntents {
    fun updateTheme(theme: SettingsValues.Theme)
}

data class UpdateSettingsState(val theme: SettingsValues.Theme)

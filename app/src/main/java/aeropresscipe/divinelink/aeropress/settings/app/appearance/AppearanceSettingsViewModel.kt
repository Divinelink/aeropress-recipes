package aeropresscipe.divinelink.aeropress.settings.app.appearance

import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.GetAvailableThemesUseCase
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.GetThemeUseCase
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.SetThemeUseCase
import aeropresscipe.divinelink.aeropress.util.WhileViewSubscribed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AppearanceSettingsViewModel @Inject constructor(
  val setThemeUseCase: SetThemeUseCase,
  getThemeUseCase: GetThemeUseCase,
  getAvailableThemesUseCase: GetAvailableThemesUseCase,
) : ViewModel() {

  private val refreshSignal = MutableSharedFlow<Unit>()
  private val loadDataSignal: Flow<Unit> = flow {
    emit(Unit)
    emitAll(refreshSignal)
  }

  private suspend fun refreshData() {
    refreshSignal.emit(Unit)
  }

  private val _uiState: StateFlow<UpdateSettingsState> = loadDataSignal.mapLatest {
    UpdateSettingsState(
      theme = getThemeUseCase(Unit).data ?: Theme.SYSTEM,
    )
  }.stateIn(
    viewModelScope,
    WhileViewSubscribed,
    initialValue = UpdateSettingsState(Theme.SYSTEM),
  )
  val uiState: StateFlow<UpdateSettingsState> = _uiState

  // Theme setting
  val availableThemes: StateFlow<List<Theme>> = loadDataSignal.mapLatest {
    getAvailableThemesUseCase(Unit).data ?: listOf()
  }.stateIn(viewModelScope, WhileViewSubscribed, listOf())

  fun setTheme(theme: Theme) {
    viewModelScope.launch {
      setThemeUseCase(theme)
      refreshData()
    }
  }
}

data class UpdateSettingsState(val theme: Theme)

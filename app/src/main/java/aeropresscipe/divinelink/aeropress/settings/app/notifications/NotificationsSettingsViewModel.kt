package aeropresscipe.divinelink.aeropress.settings.app.notifications

import aeropresscipe.divinelink.aeropress.settings.app.notifications.use_case.GetTimerSoundUseCase
import aeropresscipe.divinelink.aeropress.settings.app.notifications.use_case.SetTimerSoundUseCase
import aeropresscipe.divinelink.aeropress.util.WhileViewSubscribed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsSettingsViewModel @Inject constructor(
    getTimerSoundUseCase: GetTimerSoundUseCase,
    val setTimerSoundUseCase: SetTimerSoundUseCase
) : ViewModel() {

    private val refreshSignal = MutableSharedFlow<Unit>()
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private suspend fun refreshData() {
        refreshSignal.emit(Unit)
    }

    private val _uiState: StateFlow<NotificationSettingsState> = loadDataSignal.map {
        NotificationSettingsState(
            soundEnabled = getTimerSoundUseCase(Unit).data ?: false
        )
    }.stateIn(
        viewModelScope, WhileViewSubscribed, NotificationSettingsState(soundEnabled = false)
    )
    val uiState: StateFlow<NotificationSettingsState> = _uiState

    fun setTimerSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            setTimerSoundUseCase(enabled)
            refreshData()
        }
    }
}

data class NotificationSettingsState(
    val soundEnabled: Boolean
)

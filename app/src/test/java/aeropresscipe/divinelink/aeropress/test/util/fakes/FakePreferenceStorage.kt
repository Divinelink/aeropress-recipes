package aeropresscipe.divinelink.aeropress.test.util.fakes

import aeropresscipe.divinelink.aeropress.base.di.PreferenceStorage
import kotlinx.coroutines.flow.MutableStateFlow

open class FakePreferenceStorage(
    selectedTheme: String = "",
    timerSound: Boolean = false,
) : PreferenceStorage {

    private val _timerSound = MutableStateFlow(timerSound)
    override val timerSound = _timerSound

    private val _selectedTheme = MutableStateFlow(selectedTheme)
    override val selectedTheme = _selectedTheme

    override suspend fun selectTheme(theme: String) {
        _selectedTheme.value = theme
    }

    override suspend fun updateTimerSound(on: Boolean) {
        _timerSound.value = on
    }
}

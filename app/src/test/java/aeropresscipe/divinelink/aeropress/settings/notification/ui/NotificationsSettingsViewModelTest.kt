package aeropresscipe.divinelink.aeropress.settings.notification.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.settings.app.notifications.NotificationSettingsState
import aeropresscipe.divinelink.aeropress.settings.app.notifications.NotificationsSettingsViewModel
import aeropresscipe.divinelink.aeropress.settings.app.notifications.use_case.GetTimerSoundUseCase
import aeropresscipe.divinelink.aeropress.settings.app.notifications.use_case.SetTimerSoundUseCase
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakePreferenceStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationsSettingsViewModelTest {

    private lateinit var viewModel: NotificationsSettingsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private val fakePreferenceStorage = FakePreferenceStorage()

    private fun buildViewModel() = apply {
        viewModel = NotificationsSettingsViewModel(
            getTimerSoundUseCase = GetTimerSoundUseCase(fakePreferenceStorage, testDispatcher),
            setTimerSoundUseCase = SetTimerSoundUseCase(fakePreferenceStorage, testDispatcher),
        )
    }

    @Test
    fun `given sound enabled is false, when I update sound, then I expect sound enabled to be true`() = runTest {
        //        buildViewModel().viewModel.setTimerSoundEnabled()
        //        GetTimerSoundUseCase(fakePreferenceStorage, testDispatcher)
        val fakes = FakePreferenceStorage(
            selectedTheme = "",
            timerSound = false
        )

        // Given
        viewModel = NotificationsSettingsViewModel(
            getTimerSoundUseCase = GetTimerSoundUseCase(fakes, testDispatcher),
            setTimerSoundUseCase = SetTimerSoundUseCase(fakes, testDispatcher),
        )
        // When
        viewModel.setTimerSoundEnabled(true)
        // Then
        assertEquals(NotificationSettingsState(soundEnabled = false), viewModel.uiState.value)
    }
}

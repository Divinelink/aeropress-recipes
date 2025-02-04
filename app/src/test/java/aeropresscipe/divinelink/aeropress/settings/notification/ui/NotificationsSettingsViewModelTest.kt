package aeropresscipe.divinelink.aeropress.settings.notification.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.settings.app.notifications.NotificationSettingsState
import aeropresscipe.divinelink.aeropress.settings.app.notifications.NotificationsSettingsViewModel
import aeropresscipe.divinelink.aeropress.settings.app.notifications.usecase.GetTimerSoundUseCase
import aeropresscipe.divinelink.aeropress.settings.app.notifications.usecase.SetTimerSoundUseCase
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakePreferenceStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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

  @Test
  fun `given sound enabled is false, when I update sound, then I expect sound enabled to be true`() =
    runTest {
      val fakes = FakePreferenceStorage(
        selectedTheme = "",
        timerSound = false,
      )
      // Given
      viewModel = NotificationsSettingsViewModel(
        getTimerSoundUseCase = GetTimerSoundUseCase(fakes, testDispatcher),
        setTimerSoundUseCase = SetTimerSoundUseCase(fakes, testDispatcher),
      )
      // When
      viewModel.setTimerSoundEnabled(true)
      // Then
      assertEquals(NotificationSettingsState(soundEnabled = true), viewModel.uiState.first())
    }

  @Test
  fun `given sound enabled is true, when I update sound, then I expect sound enabled to be false`() =
    runTest {
      val fakes = FakePreferenceStorage(
        selectedTheme = "",
        timerSound = true,
      )
      // Given
      viewModel = NotificationsSettingsViewModel(
        getTimerSoundUseCase = GetTimerSoundUseCase(fakes, testDispatcher),
        setTimerSoundUseCase = SetTimerSoundUseCase(fakes, testDispatcher),
      )
      // When
      viewModel.setTimerSoundEnabled(!fakes.timerSound.value)
      // Then
      assertEquals(NotificationSettingsState(soundEnabled = false), viewModel.uiState.first())
      viewModel.setTimerSoundEnabled(!fakes.timerSound.value)
      assertEquals(NotificationSettingsState(soundEnabled = true), viewModel.uiState.first())
    }
}

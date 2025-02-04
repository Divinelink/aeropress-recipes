package aeropresscipe.divinelink.aeropress.settings.appearance.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import aeropresscipe.divinelink.aeropress.settings.app.appearance.AppearanceSettingsViewModel
import aeropresscipe.divinelink.aeropress.settings.app.appearance.UpdateSettingsState
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.GetAvailableThemesUseCase
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.GetThemeUseCase
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.SetThemeUseCase
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakePreferenceStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AppearanceSettingsViewModelTest {
  private lateinit var viewModel: AppearanceSettingsViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private fun buildViewModel(fakePreferenceStorage: FakePreferenceStorage) = apply {
    viewModel = AppearanceSettingsViewModel(
      setThemeUseCase = SetThemeUseCase(fakePreferenceStorage, testDispatcher),
      getThemeUseCase = GetThemeUseCase(fakePreferenceStorage, testDispatcher),
      getAvailableThemesUseCase = GetAvailableThemesUseCase(testDispatcher),
    )
  }

  @Test
  fun `given theme is system, when I set theme to dark, then I expect dark theme`() = runTest {
    // Given
    buildViewModel(FakePreferenceStorage(selectedTheme = Theme.SYSTEM.storageKey))
    // When
    viewModel.setTheme(Theme.DARK)
    // Then
    assertEquals(UpdateSettingsState(theme = Theme.DARK), viewModel.uiState.first())
  }

  @Test
  fun `given theme is system, then I expect system theme`() = runTest {
    // Given
    buildViewModel(FakePreferenceStorage(selectedTheme = Theme.SYSTEM.storageKey))
    // Then
    assertEquals(UpdateSettingsState(theme = Theme.SYSTEM), viewModel.uiState.first())
  }

  @Test
  fun `given theme is dark, then I expect dark theme`() = runTest {
    // Given
    buildViewModel(FakePreferenceStorage(selectedTheme = Theme.DARK.storageKey))
    // Then
    assertEquals(UpdateSettingsState(theme = Theme.DARK), viewModel.uiState.first())
  }

  @Test
  fun `given theme is light, then I expect light theme`() = runTest {
    // Given
    buildViewModel(FakePreferenceStorage(selectedTheme = Theme.LIGHT.storageKey))
    // Then
    assertEquals(UpdateSettingsState(theme = Theme.LIGHT), viewModel.uiState.first())
  }
}

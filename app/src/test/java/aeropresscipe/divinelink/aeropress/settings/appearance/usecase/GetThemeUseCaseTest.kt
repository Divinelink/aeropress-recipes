package aeropresscipe.divinelink.aeropress.settings.appearance.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.GetThemeUseCase
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakePreferenceStorage
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetThemeUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  @Before
  fun setUp() {
    fakePreferenceStorage = FakePreferenceStorage()
  }

  @Test
  fun `correctly get theme use case`() = runTest {
    // Given
    val response = Result.Success(Theme.DARK)
    val failResponse = Result.Error(Exception("Some exception"))

    fakePreferenceStorage.selectedTheme.value = response.data.storageKey

    // When
    val useCase = GetThemeUseCase(fakePreferenceStorage, testDispatcher)
    val result = useCase(Unit)
    // Then
    assertEquals(response, result)
    assertNotEquals(failResponse, result)
  }
}

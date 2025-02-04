package aeropresscipe.divinelink.aeropress.settings.notification.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.settings.app.notifications.usecase.GetTimerSoundUseCase
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
class GetTimerSoundUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  @Before
  fun setUp() {
    fakePreferenceStorage = FakePreferenceStorage()
  }

  @Test
  fun `given timer sound is false, when I get timer, then I expect timer sound to be false`() =
    runTest {
      // Given
      val response = Result.Success(false)
      val failResponse = Result.Error(Exception("Some exception"))

      fakePreferenceStorage.timerSound.value = false

      // When
      val useCase = GetTimerSoundUseCase(fakePreferenceStorage, testDispatcher)
      val result = useCase(Unit)
      // Then
      assertEquals(response, result)
      assertNotEquals(failResponse, result)
    }

  @Test
  fun `given timer sound is true, when I get timer, then I expect timer sound to be true`() =
    runTest {
      // Given
      val response = Result.Success(true)
      val failResponse = Result.Error(Exception("Some exception"))

      fakePreferenceStorage.timerSound.value = true

      // When
      val useCase = GetTimerSoundUseCase(fakePreferenceStorage, testDispatcher)
      val result = useCase(Unit)
      // Then
      assertEquals(response, result)
      assertNotEquals(failResponse, result)
    }
}

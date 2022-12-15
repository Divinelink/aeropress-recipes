package aeropresscipe.divinelink.aeropress.settings.notification.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.settings.app.notifications.usecase.SetTimerSoundUseCase
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
class SetTimerSoundUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var fakePreferenceStorage: FakePreferenceStorage

    @Before
    fun setUp() {
        fakePreferenceStorage = FakePreferenceStorage()
    }

    @Test
    fun `given timer sound is false, when I update timer, then I expect timer sound to be true`() = runTest {
        val response = Result.Success(Unit)
        val failResponse = Result.Error(Exception("Some exception"))

        fakePreferenceStorage.updateTimerSound(false)

        val useCase = SetTimerSoundUseCase(fakePreferenceStorage, testDispatcher)
        val result = useCase(true)

        assertEquals(response, result)
        assertNotEquals(failResponse, result)
    }

    @Test
    fun `given timer sound is true, when I update timer, then I expect timer sound to be false`() = runTest {
        val response = Result.Success(Unit)
        val failResponse = Result.Error(Exception("Some exception"))

        fakePreferenceStorage.updateTimerSound(true)

        val useCase = SetTimerSoundUseCase(fakePreferenceStorage, testDispatcher)
        val result = useCase(false)

        assertEquals(response, result)
        assertNotEquals(failResponse, result)
    }
}

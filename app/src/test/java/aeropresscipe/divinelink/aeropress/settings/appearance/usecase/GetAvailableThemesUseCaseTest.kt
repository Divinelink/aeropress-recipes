package aeropresscipe.divinelink.aeropress.settings.appearance.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.GetAvailableThemesUseCase
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetAvailableThemesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var fakePreferenceStorage: FakePreferenceStorage

    @Before
    fun setUp() {
        fakePreferenceStorage = FakePreferenceStorage()
    }

    @Test
    fun `given I have SDK_INT less than 29, then I see only two theme options`() = runTest {
        // Given
        val response = Result.Success(listOf(Theme.LIGHT, Theme.DARK))
        val failResponse = Result.Error(Exception("Some exception"))

        // When
        val useCase = GetAvailableThemesUseCase(testDispatcher)
        val result = useCase(Unit)

        // Then
        assertThat(response).isEqualTo(result)
        assertNotEquals(failResponse, result)
    }
}

package aeropresscipe.divinelink.aeropress.settings.appearance.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase.ObserveThemeModeUseCase
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakePreferenceStorage
import com.google.common.truth.Truth
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveThemeModeUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var fakePreferenceStorage: FakePreferenceStorage

    @Test
    fun `correct theme is observed`() = runTest {
        // Given
        val response = Result.Success(Theme.LIGHT)

        fakePreferenceStorage = FakePreferenceStorage(
            selectedTheme = Theme.LIGHT.storageKey,
        )

        // When
        val useCase = ObserveThemeModeUseCase(fakePreferenceStorage, testDispatcher)
        val result = useCase(Unit)

        // Then
        Truth.assertThat(response).isEqualTo(result.first())
    }

    @Test
    fun `correct theme is observed - dark`() = runTest {
        // Given
        val response = Result.Success(Theme.DARK)

        fakePreferenceStorage = FakePreferenceStorage(
            selectedTheme = Theme.DARK.storageKey,
        )

        // When
        val useCase = ObserveThemeModeUseCase(fakePreferenceStorage, testDispatcher)
        val result = useCase(Unit)

        // Then
        Truth.assertThat(response).isEqualTo(result.first())
    }

    @Test
    fun `correct theme is observed - system`() = runTest {
        // Given
        val response = Result.Success(Theme.SYSTEM)

        fakePreferenceStorage = FakePreferenceStorage(
            selectedTheme = Theme.SYSTEM.storageKey,
        )

        // When
        val useCase = ObserveThemeModeUseCase(fakePreferenceStorage, testDispatcher)
        val result = useCase(Unit)

        // Then
        Truth.assertThat(response).isEqualTo(result.first())
    }
}

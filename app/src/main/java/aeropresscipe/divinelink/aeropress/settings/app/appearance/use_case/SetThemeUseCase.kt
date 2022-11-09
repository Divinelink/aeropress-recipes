package aeropresscipe.divinelink.aeropress.settings.app.appearance.use_case

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.di.PreferenceStorage
import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class SetThemeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Theme, Unit>(dispatcher) {
    override suspend fun execute(parameters: Theme) {
        preferenceStorage.selectTheme(parameters.storageKey)
    }
}

package aeropresscipe.divinelink.aeropress.settings.app.appearance.use_case

import aeropresscipe.divinelink.aeropress.base.di.MainDispatcher
import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import android.os.Build
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetAvailableThemesUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, List<Theme>>(dispatcher) {

    override suspend fun execute(parameters: Unit): List<Theme> = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            listOf(Theme.SYSTEM, Theme.LIGHT, Theme.DARK)
        }
        else -> {
            listOf(Theme.LIGHT, Theme.DARK)
        }
    }
}

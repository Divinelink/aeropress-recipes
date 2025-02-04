package aeropresscipe.divinelink.aeropress.settings.app.appearance.usecase

import aeropresscipe.divinelink.aeropress.base.di.DefaultDispatcher
import aeropresscipe.divinelink.aeropress.base.di.PreferenceStorage
import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import aeropresscipe.divinelink.aeropress.base.keyvalue.themeFromStorageKey
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class ObserveThemeModeUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @DefaultDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, Theme>(dispatcher) {
  override fun execute(parameters: Unit): Flow<Result<Theme>> {
    return preferenceStorage.selectedTheme.map {
      val theme = themeFromStorageKey(it) ?: Theme.SYSTEM
      Result.Success(theme)
    }
  }
}

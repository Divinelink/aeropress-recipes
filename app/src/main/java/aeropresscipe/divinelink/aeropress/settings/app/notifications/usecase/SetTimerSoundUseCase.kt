package aeropresscipe.divinelink.aeropress.settings.app.notifications.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.di.PreferenceStorage
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class SetTimerSoundUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Boolean, Unit>(dispatcher) {
  override suspend fun execute(parameters: Boolean) {
    preferenceStorage.updateTimerSound(parameters)
  }
}

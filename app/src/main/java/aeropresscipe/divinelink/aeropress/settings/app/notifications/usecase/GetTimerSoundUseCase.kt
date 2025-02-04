package aeropresscipe.divinelink.aeropress.settings.app.notifications.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.di.PreferenceStorage
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTimerSoundUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Boolean>(dispatcher) {
  override suspend fun execute(parameters: Unit): Boolean =
    preferenceStorage.timerSound.first()
}

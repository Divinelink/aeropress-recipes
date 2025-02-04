package aeropresscipe.divinelink.aeropress.fakes

import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.RoomTimerRepository
import aeropresscipe.divinelink.aeropress.timer.TimerServices
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeTimerRepository {

  private val mockRemote: TimerServices = mock()

  val mock = RoomTimerRepository(mockRemote)

  suspend fun mockLikeRecipe(
    recipe: Recipe,
    response: Boolean,
  ) {
    whenever(mockRemote.likeRecipe(recipe)).thenReturn(response)
  }

  suspend fun mockAddToHistory(recipe: Recipe) {
    whenever(mockRemote.addToHistory(recipe)).thenReturn(Unit)
  }

  suspend fun mockIsRecipeSaved(
    recipe: Recipe?,
    response: Boolean,
  ) {
    whenever(mockRemote.isRecipeSaved(recipe)).thenReturn(response)
  }

  suspend fun mockUpdateBrewingState(
    setBrewing: Boolean,
    timeStartedMillis: Long,
    response: Unit,
  ) {
    whenever(
      mockRemote.updateBrewingState(
        brewing = setBrewing,
        timeStartedMillis = timeStartedMillis,
      ),
    ).thenReturn(response)
  }

  suspend fun mockResume(response: DiceDomain) {
    whenever(mockRemote.getResumeTimes()).thenReturn(response)
  }
}

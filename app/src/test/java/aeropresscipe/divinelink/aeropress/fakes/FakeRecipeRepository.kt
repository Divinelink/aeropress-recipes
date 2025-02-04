package aeropresscipe.divinelink.aeropress.fakes

import aeropresscipe.divinelink.aeropress.recipe.GenerateRecipeRemote
import aeropresscipe.divinelink.aeropress.recipe.RecipeRepository
import aeropresscipe.divinelink.aeropress.recipe.RoomRecipeRepository
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * A fake implementation of a [RecipeRepository] that wraps all our mock work.
 */
class FakeRecipeRepository {

  private val mockRemote: GenerateRecipeRemote = mock()

  val mock = RoomRecipeRepository(mockRemote)
  // Fixme by injecting a FakeRecipeRepository instead of passing mockRemote

  suspend fun mockCreateNewRecipe(
    response: DiceDomain,
  ) {
    whenever(mockRemote.createNewRecipe()).thenReturn(response)
  }

  suspend fun mockGetRecipe(
    response: DiceDomain?,
  ) {
    whenever(mockRemote.getRecipe()).thenReturn(response)
  }

  suspend fun mockGenerateRecipe(
    response: Boolean,
  ) {
    whenever(mockRemote.alreadyBrewing()).thenReturn(response)
  }
}

package aeropresscipe.divinelink.aeropress.fakes

import aeropresscipe.divinelink.aeropress.home.HomeRemote
import aeropresscipe.divinelink.aeropress.home.HomeRepository
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeHomeRepository {

    private val mockRemote: HomeRemote = mock()

    val mock = HomeRepository(mockRemote)

    suspend fun mockGetRecipe(
        response: DiceDomain,
    ) {
        whenever(mockRemote.getRecipe()).thenReturn(response)
    }

    suspend fun mockUpdateRecipe(
        recipe: Recipe,
        update: Boolean,
        response: DiceDomain,
    ) {
        whenever(
            mockRemote.updateRecipe(
                recipe = recipe,
                update = update,
            )
        ).thenReturn(
            response
        )
    }
}

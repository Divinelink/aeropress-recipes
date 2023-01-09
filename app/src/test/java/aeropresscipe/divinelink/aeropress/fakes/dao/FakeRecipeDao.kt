package aeropresscipe.divinelink.aeropress.fakes.dao

import aeropresscipe.divinelink.aeropress.recipe.RecipeDao
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FakeRecipeDao {

    val mock: RecipeDao = mock()

    suspend fun mockGetRecipe(
        response: DiceDomain,
    ) {
        whenever(
            mock.getRecipe()
        ).thenReturn(
            response
        )
    }

    suspend fun verifyGetRecipe() {
        verify(mock).getRecipe()
    }

    suspend fun mockUpdateRecipe(
        recipe: Recipe,
    ) {
        whenever(
            mock.updateRecipe(recipe)
        ).thenReturn(
            Unit
        )
    }

    suspend fun verifyUpdateRecipe(recipe: Recipe) {
        verify(mock).updateRecipe(recipe)
    }
}

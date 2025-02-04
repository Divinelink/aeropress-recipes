package aeropresscipe.divinelink.aeropress.models

import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.recipe.models.RecipeStep
import aeropresscipe.divinelink.aeropress.recipe.models.buildSteps
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RecipeTest {

  @Test
  fun `inverted method is correctly applied on recipe`() {
    val recipe = Recipe(
      diceTemperature = 10,
      brewTime = 10,
      bloomTime = 10,
      bloomWater = 10,
      coffeeAmount = 10,
      brewWaterAmount = 10,
      grindSize = CoffeeGrindSize.COARSE,
      brewMethod = BrewMethod.INVERTED,
    )
    val steps = recipe.buildSteps()
    assertTrue(steps.contains(RecipeStep.InvertedMethodStep))
    assertTrue(steps.contains(RecipeStep.FlipToNormalOrientation))
  }

  @Test
  fun `standard method is correctly applied on recipe`() {
    val recipe = Recipe(
      diceTemperature = 10,
      brewTime = 10,
      bloomTime = 10,
      bloomWater = 10,
      coffeeAmount = 10,
      brewWaterAmount = 10,
      grindSize = CoffeeGrindSize.COARSE,
      brewMethod = BrewMethod.STANDARD,
    )
    val steps = recipe.buildSteps()
    assertTrue(steps.contains(RecipeStep.StandardMethodStep))
    assertFalse(steps.contains(RecipeStep.FlipToNormalOrientation))
  }

  @Test
  fun `when recipe has bloom then bloom step is applied`() {
    val recipe = Recipe(
      diceTemperature = 10,
      brewTime = 10,
      bloomTime = 10,
      bloomWater = 10,
      coffeeAmount = 10,
      brewWaterAmount = 10,
      grindSize = CoffeeGrindSize.COARSE,
      brewMethod = BrewMethod.STANDARD,
    )
    val steps = recipe.buildSteps()
    assertTrue(
      steps.contains(
        RecipeStep.BloomStep(
          recipe.bloomWater,
          bloomTime = recipe.bloomTime,
        ),
      ),
    )
  }
}

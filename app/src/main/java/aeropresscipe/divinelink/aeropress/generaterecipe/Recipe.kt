package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import androidx.room.Ignore
import java.io.Serializable

data class Recipe(
    var diceTemperature: Int,
    var brewTime: Int,
    var bloomTime: Int,
    var bloomWater: Int,
    var coffeeAmount: Int,
    var brewWaterAmount: Int,
    var grindSize: CoffeeGrindSize,
    var brewMethod: BrewMethod,
    @Ignore var isNewRecipe: Boolean = false,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

fun Recipe.withBloom(): Boolean {
    return this.bloomTime != 0
}

fun Recipe.remainingWater(): Int {
    return this.brewWaterAmount - this.bloomWater
}

fun Recipe.getBrewingStates(): MutableList<BrewState> {
    val brewStates: MutableList<BrewState> = mutableListOf()
    if (this.withBloom()) {
        brewStates.add(BrewState.Bloom(water = this.bloomWater, time = this.bloomTime.toLong()))
        brewStates.add(BrewState.BrewWithBloom(water = this.remainingWater(), time = this.brewTime.toLong()))
    } else {
        brewStates.add(BrewState.Brew(water = this.brewWaterAmount, time = this.brewTime.toLong()))
    }
    brewStates.add(BrewState.Finished)
    return brewStates
}

fun Recipe.buildSteps(): MutableList<RecipeStep> {
    val steps: MutableList<RecipeStep> = mutableListOf()

    steps.add(RecipeStep.HeatWaterStep(this.brewWaterAmount, this.diceTemperature))
    steps.add(RecipeStep.CoffeeGrindStep(
        coffeeAmount = this.coffeeAmount,
        grindSize = this.grindSize)
    )

    when (this.brewMethod) {
        BrewMethod.STANDARD -> steps.add(RecipeStep.StandardMethodStep)
        BrewMethod.INVERTED -> steps.add(RecipeStep.InvertedMethodStep)
    }

    steps.add(RecipeStep.PourGroundCoffeeStep)
    if (this.withBloom()) {
        steps.add(RecipeStep.BloomStep(bloomWater = this.bloomWater, bloomTime = this.bloomTime))
        steps.add(RecipeStep.RemainingWaterStep(remainingWater = this.remainingWater()))
    } else {
        steps.add(RecipeStep.PourWaterStep(waterAmount = this.remainingWater()))
    }

    steps.add(RecipeStep.WaitToBrewStep(brewTime = this.brewTime))

    if (this.brewMethod == BrewMethod.INVERTED) {
        steps.add(RecipeStep.FlipToNormalOrientation)
    }

    steps.add(RecipeStep.PressStep)

    return steps
}

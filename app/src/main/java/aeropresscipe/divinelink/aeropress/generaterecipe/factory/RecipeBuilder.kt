package aeropresscipe.divinelink.aeropress.generaterecipe.factory

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Dices
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.generaterecipe.models.RecipeDice

class RecipeBuilder {
    val recipe = Builder().build(
        temperature = getDice(Dices.TEMPERATURE) as RecipeDice.TemperatureDice,
        groundSize = getDice(Dices.GROUND_SIZE) as RecipeDice.GroundSizeDice,
        method = getDice(Dices.METHOD) as RecipeDice.MethodDice,
        brewWater = getDice(Dices.BREW_WATER) as RecipeDice.BrewWaterDice
    )

    data class Builder(
        var temperature: RecipeDice.TemperatureDice? = null,
        var groundSize: RecipeDice.GroundSizeDice? = null,
        var method: RecipeDice.MethodDice? = null,
        var brewWater: RecipeDice.BrewWaterDice? = null,
    ) {
        fun build(
            temperature: RecipeDice.TemperatureDice,
            groundSize: RecipeDice.GroundSizeDice,
            method: RecipeDice.MethodDice,
            brewWater: RecipeDice.BrewWaterDice
        ) = Recipe(
            diceTemperature = temperature.temperature,
            brewTime = groundSize.brewTime.toLong(),
            bloomTime = method.bloomTime.toLong(),
            bloomWater = method.bloomWater,
            coffeeAmount = brewWater.coffeeAmount,
            brewWaterAmount = brewWater.brewWater,
            grindSize = groundSize.groundSize,
            brewMethod = method.brewMethod
        )
    }

    private fun getDice(diceType: Dices): RecipeDice {
        return when (diceType) {
            Dices.METHOD -> BrewMethodDice().rollDice()
            Dices.TEMPERATURE -> Temperature().rollDice()
            Dices.GROUND_SIZE -> GroundSize().rollDice()
            Dices.BREW_WATER -> BrewWaterAmountDice().rollDice()
        }
    }
}

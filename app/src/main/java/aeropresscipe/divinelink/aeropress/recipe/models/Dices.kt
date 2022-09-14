package aeropresscipe.divinelink.aeropress.recipe.models

enum class BrewMethod(val method: String) {
    STANDARD("Standard"),
    INVERTED("Inverted")
}

enum class CoffeeGrindSize(val size: String) {
    FINE("fine"),
    MEDIUM_FINE("medium - fine"),
    MEDIUM("medium"),
    COARSE("coarse"),
}

sealed class RecipeDice {

    data class MethodDice(
        val brewMethod: BrewMethod,
        val bloomTime: Int,
        val bloomWater: Int
    ) : RecipeDice()

    data class TemperatureDice(
        val temperature: Int
    ) : RecipeDice()

    data class GroundSizeDice(
        val groundSize: CoffeeGrindSize,
        val brewTime: Int
    ) : RecipeDice()


    data class BrewWaterDice(
        val coffeeAmount: Int,
        val brewWater: Int
    ) : RecipeDice()

}

enum class Dices {
    METHOD,
    TEMPERATURE,
    GROUND_SIZE,
    BREW_WATER
}

package aeropresscipe.divinelink.aeropress.generaterecipe.models

enum class BrewMethod(val method: String) {
    STANDARD("Standard"),
    INVERTED("Inverted")
}

enum class CoffeeGrindSize(val size: String) {
    FINE("fine"),
    MEDIUM_FINE("medium-fine"),
    MEDIUM("medium"),
    COARSE("coarse"),
}

data class MethodDice(
    val brewMethod: BrewMethod,
    val bloomTime: Int,
    val bloomWater: Int
)

data class TemperatureDice(
    val temperature: Int
)

data class GroundSizeDice(
    val groundSize: CoffeeGrindSize,
    val brewTime: Int
)

data class BrewWaterDice(
    val coffeeAmount: Int,
    val brewWater: Int
)

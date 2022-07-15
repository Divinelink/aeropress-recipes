package aeropresscipe.divinelink.aeropress.generaterecipe

enum class BrewMethod {
    Standard,
    Inverted
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
    val groundSize: String,
    val brewTime: Int
)

data class BrewWaterDice(
    val coffeeAmount: Int,
    val brewWater: Int
)

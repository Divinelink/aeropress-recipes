package aeropresscipe.divinelink.aeropress.generaterecipe

data class MethodDice(
    val brewingMethod: String,
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
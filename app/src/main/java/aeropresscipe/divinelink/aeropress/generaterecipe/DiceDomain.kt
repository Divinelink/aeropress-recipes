package aeropresscipe.divinelink.aeropress.generaterecipe

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Recipe")
data class DiceDomain(
    @PrimaryKey(autoGenerate = true)
    // FIXME Fix cycle in the delegation calls chain
    var id: Int = 0,
    var diceTemperature: Int = 0,
    var brewTime: Int = 0,
    var bloomTime: Int = 0,
    var bloomWater: Int = 0,
    var coffeeAmount: Int = 0,
    var brewWaterAmount: Int = 0,
    var groundSize: String = "",
    var brewingMethod: String = ""
) {

    private constructor(
        diceTemperature: Int,
        id: Int,
        groundSize: String,
        brewTime: Int,
        brewingMethod: String,
        bloomTime: Int,
        bloomWater: Int,
        brewWaterAmount: Int,
        coffeeAmount: Int
    ) : this(
        id,
        diceTemperature,
        brewTime,
        bloomTime,
        bloomWater,
        coffeeAmount,
        brewWaterAmount,
        groundSize,
        brewingMethod
    ) {
        this.id = id
        this.diceTemperature = diceTemperature
        this.groundSize = groundSize
        this.brewTime = brewTime
        this.brewingMethod = brewingMethod
        this.bloomTime = bloomTime
        this.bloomWater = bloomWater
        this.brewWaterAmount = brewWaterAmount
        this.coffeeAmount = coffeeAmount
    }

    @Ignore
    constructor(
        diceTemperature: Int,
        groundSize: String,
        brewTime: Int,
        brewingMethod: String,
        bloomTime: Int,
        bloomWater: Int,
        brewWaterAmount: Int,
        coffeeAmount: Int
    ) : this() {
        this.diceTemperature = diceTemperature
        this.groundSize = groundSize
        this.brewTime = brewTime
        this.brewingMethod = brewingMethod
        this.bloomTime = bloomTime
        this.bloomWater = bloomWater
        this.brewWaterAmount = brewWaterAmount
        this.coffeeAmount = coffeeAmount
    }

    @Ignore
    constructor(groundSize: String, brewTime: Int) : this() {
        this.groundSize = groundSize
        this.brewTime = brewTime
    }

    @Ignore
    constructor(coffeeAmount: Int, brewWaterAmount: Int) : this() {
        this.coffeeAmount = coffeeAmount
        this.brewWaterAmount = brewWaterAmount
    }

    @Ignore
    constructor(diceTemperature: Int) : this() {
        this.diceTemperature = diceTemperature
    }

    @Ignore
    constructor(brewingMethod: String, bloomTime: Int, bloomWater: Int) : this() {
        this.brewingMethod = brewingMethod
        this.bloomTime = bloomTime
        this.bloomWater = bloomWater
    }
}

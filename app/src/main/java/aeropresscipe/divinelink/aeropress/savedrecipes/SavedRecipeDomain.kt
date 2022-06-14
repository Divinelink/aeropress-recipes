package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedRecipes")
data class SavedRecipeDomain(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var diceTemperature: Int,
    var brewTime: Int,
    var bloomTime: Int,
    var bloomWater: Int,
    var coffeeAmount: Int,
    var brewWaterAmount: Int,
    var groundSize: String,
    var brewingMethod: String,
    var dateBrewed: String,

    // FIXME figure out how to remove diceDomain field from DB
    var diceDomain: DiceDomain? = null,
) {
    constructor(diceDomain: DiceDomain, dateBrewed: String) : this(
        diceDomain = diceDomain,
        dateBrewed = dateBrewed,
        id = diceDomain.id,
        diceTemperature = diceDomain.diceTemperature,
        groundSize = diceDomain.groundSize,
        brewTime = diceDomain.brewTime,
        brewingMethod = diceDomain.brewingMethod,
        bloomTime = diceDomain.bloomTime,
        bloomWater = diceDomain.bloomWater,
        brewWaterAmount = diceDomain.brewWaterAmount,
        coffeeAmount = diceDomain.coffeeAmount,
    )
}

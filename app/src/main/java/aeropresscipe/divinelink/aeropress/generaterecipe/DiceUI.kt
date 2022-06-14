package aeropresscipe.divinelink.aeropress.generaterecipe

import java.io.Serializable

class DiceUI(
    var bloomTime: Int,
    var brewTime: Int,
    var bloomWater: Int,
    var remainingBrewWater: Int,
    var isNewRecipe: Boolean = false
) : Serializable {

    private var recipeHadBloom = false

    fun recipeHadBloom(): Boolean {
        return recipeHadBloom
    }

    fun setRecipeHadBloom(recipeHadBloom: Boolean) {
        this.recipeHadBloom = recipeHadBloom
    }
}

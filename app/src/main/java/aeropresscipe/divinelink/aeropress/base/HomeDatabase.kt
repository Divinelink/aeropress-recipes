package aeropresscipe.divinelink.aeropress.base

import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.history.History
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import aeropresscipe.divinelink.aeropress.savedrecipes.Converters
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DiceDomain::class, SavedRecipeDomain::class, History::class],
    version = 22,
    exportSchema = true
)
@TypeConverters(
    Converters::class
)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun savedRecipeDao(): SavedRecipeDao
    abstract fun historyDao(): HistoryDao

    companion object {
        const val DB_NAME = "Home_Database"
    }
}

package aeropresscipe.divinelink.aeropress.base

import androidx.room.Database
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import aeropresscipe.divinelink.aeropress.history.HistoryDomain
import androidx.room.TypeConverters
import aeropresscipe.divinelink.aeropress.savedrecipes.Converters
import androidx.room.RoomDatabase
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import android.content.Context
import androidx.room.Room

@Database(
    entities = [DiceDomain::class, SavedRecipeDomain::class, HistoryDomain::class],
    version = 13,
    exportSchema = false
)
@TypeConverters(
    Converters::class
)
abstract class HomeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao?
    abstract fun savedRecipeDao(): SavedRecipeDao?
    abstract fun historyDao(): HistoryDao?

    companion object {
        private var INSTANCE: HomeDatabase? = null
        @JvmStatic
        fun getDatabase(ctx: Context): HomeDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    ctx.applicationContext,
                    HomeDatabase::class.java, "Home_Database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }
    }
}

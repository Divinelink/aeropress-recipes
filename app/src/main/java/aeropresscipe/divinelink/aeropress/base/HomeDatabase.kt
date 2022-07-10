package aeropresscipe.divinelink.aeropress.base

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import aeropresscipe.divinelink.aeropress.history.History
import aeropresscipe.divinelink.aeropress.savedrecipes.Converters
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DiceDomain::class, SavedRecipeDomain::class, History::class],
    version = 18,
    exportSchema = false
)
@TypeConverters(
    Converters::class
)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun savedRecipeDao(): SavedRecipeDao
    abstract fun historyDao(): HistoryDao

    companion object {
        private var INSTANCE: HomeDatabase? = null
        const val DB_NAME = "Home_Database"

        @JvmStatic
        fun getDatabase(ctx: Context): HomeDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    ctx.applicationContext,
                    HomeDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}

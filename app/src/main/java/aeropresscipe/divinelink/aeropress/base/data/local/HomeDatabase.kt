package aeropresscipe.divinelink.aeropress.base.data.local

import aeropresscipe.divinelink.aeropress.base.data.local.bean.BeanDAO
import aeropresscipe.divinelink.aeropress.base.data.local.bean.PersistableBean
import aeropresscipe.divinelink.aeropress.favorites.Converters
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.favorites.FavoritesDao
import aeropresscipe.divinelink.aeropress.history.History
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import aeropresscipe.divinelink.aeropress.recipe.RecipeDao
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        DiceDomain::class,
        Favorites::class,
        History::class,
        PersistableBean::class,
    ],
    version = HomeDatabase.LATEST_VERSION,
    exportSchema = true
)
@TypeConverters(
    Converters::class
)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun historyDao(): HistoryDao
    abstract fun beanDAO(): BeanDAO

    companion object {
        const val DB_NAME = "Home_Database"
        const val LATEST_VERSION = 23
    }
}

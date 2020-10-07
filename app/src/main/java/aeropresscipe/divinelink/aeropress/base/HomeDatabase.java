package aeropresscipe.divinelink.aeropress.base;

import android.content.Context;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = DiceDomain.class, version = 3, exportSchema = false)
abstract public class HomeDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

    static private HomeDatabase INSTANCE;

    public static HomeDatabase getDatabase(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(),
                    HomeDatabase.class, "Home_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}

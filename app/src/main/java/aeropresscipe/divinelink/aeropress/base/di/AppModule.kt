package aeropresscipe.divinelink.aeropress.base.di

import aeropresscipe.divinelink.aeropress.base.data.local.HomeDatabase
import aeropresscipe.divinelink.aeropress.base.data.local.HomeDatabase.Companion.DB_NAME
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationComponent (i.e. everywhere in the application)
  @Provides
  fun provideYourDatabase(
    @ApplicationContext app: Context,
  ) = Room.databaseBuilder(
    app,
    HomeDatabase::class.java,
    DB_NAME,
  ).fallbackToDestructiveMigration().build() // The reason we can construct a database for the repo

  @Singleton
  @Provides
  fun provideHistoryDao(db: HomeDatabase) = db.historyDao()

  @Singleton
  @Provides
  fun provideRecipeDao(db: HomeDatabase) = db.recipeDao()

  @Singleton
  @Provides
  fun provideSavedRecipeDao(db: HomeDatabase) = db.favoritesDao()

  @Singleton
  @Provides
  fun provideBeanDAO(db: HomeDatabase) = db.beanDAO()

  @ApplicationContext
  @Provides
  fun providesApplicationContext() = Application()

  @ApplicationScope
  @Singleton
  @Provides
  fun providesApplicationScope(
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
  ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

  @Provides
  @Singleton
  fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
    return context.getSharedPreferences(
      context.packageName + "_preferences",
      Context.MODE_PRIVATE,
    )
  }
}

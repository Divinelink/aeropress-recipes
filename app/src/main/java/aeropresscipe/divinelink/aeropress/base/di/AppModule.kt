package aeropresscipe.divinelink.aeropress.base.di

import aeropresscipe.divinelink.aeropress.base.HomeDatabase
import aeropresscipe.divinelink.aeropress.base.HomeDatabase.Companion.DB_NAME
import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationComponent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        HomeDatabase::class.java,
        DB_NAME
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideHistoryDao(db: HomeDatabase) = db.historyDao()

    @Singleton
    @Provides
    fun provideRecipeDao(db: HomeDatabase) = db.recipeDao()

    @Singleton
    @Provides
    fun provideSavedRecipeDao(db: HomeDatabase) = db.savedRecipeDao()

    @ApplicationContext
    @Provides
    fun providesApplicationContext() = Application()

}
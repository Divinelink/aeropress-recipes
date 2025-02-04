package aeropresscipe.divinelink.aeropress.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

  @Singleton
  @Provides
  fun provideClock(): Clock = Clock.System
}

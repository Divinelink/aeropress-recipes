package aeropresscipe.divinelink.aeropress.base.di

import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module is responsible for defining the creation of any repository dependencies used in the
 * application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBeanRepository(
        beanRepository: BeanRepository,
    ): BeanRepository
}

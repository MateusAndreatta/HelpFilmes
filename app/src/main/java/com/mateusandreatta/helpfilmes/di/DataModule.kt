package com.mateusandreatta.helpfilmes.di

import com.mateusandreatta.helpfilmes.repository.HomeDataSource
import com.mateusandreatta.helpfilmes.repository.HomeDataSourceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {
    @Singleton
    @Binds
    abstract fun provideHomeDataSource(dataSource: HomeDataSourceImpl) : HomeDataSource
}
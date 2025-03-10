package com.timife.cowryconverter.data.di

import com.timife.cowryconverter.data.repositoryImpls.ConverterRepoImpl
import com.timife.cowryconverter.domain.repositories.ConverterRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {

    @Binds
    @Singleton
    abstract fun bindConverterRepo(
        converterRepoImpl: ConverterRepoImpl
    ): ConverterRepo
}
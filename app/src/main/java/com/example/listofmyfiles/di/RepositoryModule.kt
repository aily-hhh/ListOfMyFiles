package com.example.listofmyfiles.di

import com.example.listofmyfiles.data.dao.FileDao
import com.example.listofmyfiles.data.repository.FileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFileRepository(): FileDao {
        return FileRepository()
    }
}
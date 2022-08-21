package com.bootcamp.todoeasy.di


import android.content.Context
import androidx.room.Room
import com.bootcamp.todoeasy.data.room.TaskDataSource
import com.bootcamp.todoeasy.data.room.TaskDataSourceImp
import com.bootcamp.todoeasy.data.room.TaskDatabase
import com.bootcamp.todoeasy.domain.Repository
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providerDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TaskDatabase::class.java,
        "task_database"
    ).build()

    @Singleton
    @Provides
    fun providerTaskDao(database: TaskDatabase) = database.taskDao()

    @Singleton
    @LocalTaskDataSource
    @Provides
    fun provideLocalTaskDataSource(
        database: TaskDatabase
    ): TaskDataSource {
        return TaskDataSourceImp(database.taskDao())
    }

    @Singleton
    @Provides
    fun provideTaskRepository(
        @LocalTaskDataSource taskDataSourceImp: TaskDataSourceImp,
    ): Repository {
        return RepositoryImp(taskDataSourceImp)
    }

}


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalTaskDataSource
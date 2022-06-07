package dev.robert.taskify.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.taskify.data.TaskDatabase
import dev.robert.taskify.repository.TasksRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {
    @Provides
    @Singleton
    fun provideRepository(database: TaskDatabase): TasksRepository {
        return TasksRepository(database)
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(
            application,
            TaskDatabase::class.java,
            "tasks.db"
        )
            .build()
}
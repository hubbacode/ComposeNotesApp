package com.paliy.composenotes.di

import android.content.Context
import androidx.room.Room
import com.paliy.composenotes.data.NotesDao
import com.paliy.composenotes.data.NotesDatabase
import com.paliy.composenotes.data.NotesRepositoryImpl
import com.paliy.composenotes.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsNotesRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository

    companion object{

        @Singleton
        @Provides
        fun provideNotesDatabase(
            @ApplicationContext context: Context
        ): NotesDatabase{
            return Room.databaseBuilder(
                context = context,
                klass = NotesDatabase::class.java,
                name = "notes.db"
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        //NotesDatabase.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideNotesDao(
            database: NotesDatabase
        ): NotesDao{
            return database.notesDao()
        }
    }
}


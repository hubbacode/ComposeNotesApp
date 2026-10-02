package com.paliy.composenotes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteWithContentDbModel>>

    @Query("SELECT * FROM notes WHERE id == :noteId")
    suspend fun getNote(noteId: Int): NoteWithContentDbModel

    @Query("""
        SELECT DISTINCT notes.* FROM notes JOIN content ON notes.id == content.noteId
        WHERE title LIKE '%' || :query || '%'
        OR content LIKE '%' || :query || '%' 
        ORDER BY updatedAt DESC""")
    fun searchNotes(query: String): Flow<List<NoteWithContentDbModel>>

    @Query("DELETE FROM notes WHERE id == :noteId")
    suspend fun deleteNote(noteId : Int)

    @Query("UPDATE notes SET isPinned = NOT isPinned WHERE id == :noteId")
    suspend fun switchPinnedStatus(noteId : Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteDbModel: NoteDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNoteContent(content: List<ContentItemDbModel>)

    @Query("DELETE FROM content WHERE noteId == :noteId")
    suspend fun deleteNoteContent(noteId : Int)
}
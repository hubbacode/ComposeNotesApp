package com.paliy.composenotes.data

import android.content.Context
import com.paliy.composenotes.domain.ContentItem
import com.paliy.composenotes.domain.Note
import com.paliy.composenotes.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
    private val imageFileManager: ImageFileManager
): NotesRepository {

    //private val notesDatabase = NotesDatabase.getInstance(context)
    //private val notesDao = notesDatabase.notesDao()

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updatedAt: Long
    ) {
        val processedContent = content.processForStorage()
        val noteDbModel = NoteDbModel(0, title, updatedAt, isPinned)
        notesDao.addNoteWithContext(noteDbModel,processedContent)
    }

    override suspend fun deleteNote(noteId: Int) {
        val note = notesDao.getNote(noteId).toEntity()
        notesDao.deleteNote(noteId)

        note.content
            .filterIsInstance<ContentItem.Image>()
            .map { it.uri }
            .forEach {
                imageFileManager.deleteImage(it)
            }
    }

    override suspend fun editNote(note: Note) {
        val oldNote = notesDao.getNote(note.id).toEntity()

        val oldUrls = oldNote.content.filterIsInstance<ContentItem.Image>().map { it.uri }
        val newUrls = note.content.filterIsInstance<ContentItem.Image>().map { it.uri }
        val removeUrls = oldUrls - newUrls

        removeUrls.forEach {
            imageFileManager.deleteImage(it)
        }

        val processedContent = note.content.processForStorage()
        val processedNote = note.copy(content = processedContent)
        notesDao.editNoteWithContext(
            noteDbModel = processedNote.toDbModel(),
            content = processedContent.toContentItemDbModels(noteId = note.id))

    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map { it.toEntities() }
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map { it.toEntities() }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    private suspend fun List<ContentItem>.processForStorage(): List<ContentItem>{
        return map {contentItem ->
            when(contentItem) {
                is ContentItem.Image -> {
                    if (imageFileManager.isInternal(contentItem.uri)){
                        contentItem
                    }else{
                        val internalPath =
                            imageFileManager.copyImageToInternalStorage(contentItem.uri)
                        ContentItem.Image(internalPath)
                    }
                }
                is ContentItem.Text -> contentItem
            }
        }
    }

    /*companion object{
        private val LOCK = Any()
        private var instance: NotesRepositoryImpl? = null

        fun getInstance(context: Context): NotesRepositoryImpl{
            instance?.let { return it }

            synchronized(LOCK){
                instance?.let { return it }
                return NotesRepositoryImpl(context).also {
                    instance = it
                }
            }
        }
    }*/

}
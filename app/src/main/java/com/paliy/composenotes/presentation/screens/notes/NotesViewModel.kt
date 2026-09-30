@file:OptIn(ExperimentalCoroutinesApi::class)

package com.paliy.composenotes.presentation.screens.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paliy.composenotes.data.NotesRepositoryImpl
import com.paliy.composenotes.domain.AddNoteUseCase
import com.paliy.composenotes.domain.DeleteNoteUseCase
import com.paliy.composenotes.domain.EditNoteUseCase
import com.paliy.composenotes.domain.GetAllNotesUseCase
import com.paliy.composenotes.domain.GetNoteUseCase
import com.paliy.composenotes.domain.Note
import com.paliy.composenotes.domain.SearchNotesUseCase
import com.paliy.composenotes.domain.SwitchPinnedStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val switchPinnedStatusUseCase: SwitchPinnedStatusUseCase
) : ViewModel() {

    //private val repository = NotesRepositoryImpl.getInstance(context)

    //private val addNoteUseCase = AddNoteUseCase(repository)
    //private val editNoteUseCase = EditNoteUseCase(repository)
    //private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    //private val getNoteUseCase = GetNoteUseCase(repository)

    /* HiltViewModelInject
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val searchNotesUseCase = SearchNotesUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)
     */

    private val query = MutableStateFlow("")

    private val _state = MutableStateFlow(NotesScreenState())
    val state = _state.asStateFlow()


    init {
        query.onEach { input ->
            _state.update { it.copy(query = input) }
        }
            .flatMapLatest { input ->
                if (input.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNotesUseCase(input)
                }
            }
            .onEach { notes ->
                val pinnedNotes = notes.filter { it.isPinned }
                val otherNotes = notes.filter { !it.isPinned }
                _state.update { it.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes) }
            }
            .launchIn(viewModelScope)
    }
    /*
    private fun addSomeNotes(){
        viewModelScope.launch {
            repeat(10){
                addNoteUseCase(title = "Title №$it", content = "Content №$it")
            }
        }

    }*/


    fun processCommand(command: NotesCommand){
        viewModelScope.launch {
            when(command) {
                /*is NotesCommand.DeleteNote ->{
                    deleteNoteUseCase(command.noteId)
                }
                is NotesCommand.EditNote ->{
                    val note = getNoteUseCase(command.note.id)
                    val title = note.title
                    editNoteUseCase(note.copy(title = title + "$title edited"))
                }*/
                is NotesCommand.InputSearchQuery ->{
                    query.update { command.query.trim() }
                }
                is NotesCommand.SwitchPinnedStatus ->{
                    switchPinnedStatusUseCase(command.noteId)
                }
            }
        }

    }
}

sealed interface NotesCommand {
    data class InputSearchQuery(val query: String): NotesCommand
    data class SwitchPinnedStatus(val noteId : Int): NotesCommand

    /*data class DeleteNote(val noteId: Int): NotesCommand
    data class EditNote(val note: Note): NotesCommand*/
}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf()
    )
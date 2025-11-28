package com.example.notesApp.service;

import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NoteService {
    Note createNote(@Valid Note note);
    void updateNote(String id, Note note);
    void deleteNoteById(String id);
    List<NoteDto> getAllNotes();
    Optional<NoteDetailsDto> getNoteDetailsDTOById(String id);
    Map<String, Long> getWordStats(String id);
    List<NoteDto> getAllNotesSorted();
    List<NoteDto> getNotesByTag(Tags tag);
    List<NoteDto> getNotesPage(Pageable pageable);
}

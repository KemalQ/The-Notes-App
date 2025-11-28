package com.example.notesApp.service;

import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteSummaryDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NoteService {
    Note createNote(Note note);
    void updateNote(String id, Note note);
    void deleteNoteById(String id);
    List<NoteSummaryDto> getAllNotes();
    Optional<NoteDetailsDto> getNoteDetailsDTOById(String id);
    Map<String, Long> getWordStats(String id);
    List<Note> getAllNotesSorted();
    List<Note> getNotesByTag(Tags tag);
    List<Note> getNotesPage(Pageable pageable);
}

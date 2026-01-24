package com.example.notesApp.service;

import com.example.notesApp.dto.*;
import com.example.notesApp.enums.Tags;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface NoteService {
    CreateNoteResponseDto createNote(CreateNoteDto note);
    void updateNote(String id, PutNoteDto putNoteDto);
    void deleteNoteById(String id);
    NoteDetailsDto getNoteDetailsDTOById(String id);
    Map<String, Long> getWordStats(String id);

    List<NoteDto> getNotes(Tags tags, Pageable pageable);
}

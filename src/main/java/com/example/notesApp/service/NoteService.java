package com.example.notesApp.service;

import com.example.notesApp.dto.CreateNoteDto;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.dto.PutNoteDto;
import com.example.notesApp.enums.Tags;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface NoteService {
    NoteDto createNote(@Valid CreateNoteDto note);
    void updateNote(String id, PutNoteDto putNoteDto);
    void deleteNoteById(String id);
    NoteDetailsDto getNoteDetailsDTOById(String id);
    Map<String, Long> getWordStats(String id);

    List<NoteDto> getNotes(Tags tags, Pageable pageable);
}

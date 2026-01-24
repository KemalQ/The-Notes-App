package com.example.notesApp.controller;

import com.example.notesApp.dto.*;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.service.NoteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NoteService noteService;

    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<CreateNoteResponseDto> createNote(@Valid @RequestBody CreateNoteDto note){
        CreateNoteResponseDto savedNote = noteService.createNote(note);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNote.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedNote);
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes(
            @RequestParam(required = false) Tags tag, Pageable pageable) {

        log.info("Getting notes page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.ok(noteService.getNotes(tag, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDetailsDto> getNoteDetails(@PathVariable String id) {
        log.info("Getting note details for id: {}", id);
        return ResponseEntity.ok(noteService.getNoteDetailsDTOById(id));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Long>> noteStats(@PathVariable String id){
        log.info("Note stats retrieved for id: {}", id);
        return ResponseEntity.ok(noteService.getWordStats(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNote(@PathVariable String id, @Valid @RequestBody PutNoteDto putNoteDto){
        noteService.updateNote(id, putNoteDto);
        log.info("Note updated! id = {}", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id){
        noteService.deleteNoteById(id);
        log.info("Note deleted! id = {}", id);
        return ResponseEntity.noContent().build();
    }
}

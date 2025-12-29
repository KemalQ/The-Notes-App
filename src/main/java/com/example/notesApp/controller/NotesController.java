package com.example.notesApp.controller;

import com.example.notesApp.dto.CreateNoteDto;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.dto.PutNoteDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.mapper.NoteMapper;
import com.example.notesApp.service.NoteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private final NoteMapper noteMapper;

    public NotesController(NoteService noteService, NoteMapper noteMapper) {
        this.noteService = noteService;
        this.noteMapper = noteMapper;
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody CreateNoteDto note){
        NoteDto savedNote = noteService.createNote(note);

//        URI location = ServletUriComponentsBuilder//TODO добавить возвращение с id созданного ресурса
//                .fromCurrentRequest().path("/id").buildAndExpand(savedNote.getTitle()).toUri();
        return new ResponseEntity<>(savedNote, HttpStatus.CREATED);
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

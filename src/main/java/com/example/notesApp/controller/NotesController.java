package com.example.notesApp.controller;

import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.mapper.NoteMapper;
import com.example.notesApp.model.Note;
import com.example.notesApp.service.NoteService;
import com.example.notesApp.service.impl.NoteServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody Note note){
        // Step 1. Saving entity in DB and Create entity variable
        Note savedNote = noteService.createNote(note);

        //  Step 2. note-> noteDto
        NoteDto savedNoteDto = noteMapper.toNoteDto(savedNote);

        // Step 3. Creating URI location for saved entity (NOT DTO!)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNote.getId())
                .toUri();

        // Step 4. Returning status code and DTO in body, setting HTTP Header location
        // (ResponseEntity: HTTP Status Code, request body, HTTP Header)
        return ResponseEntity.created(location).body(savedNoteDto);
    }

    @GetMapping("/fullinfo")
    public ResponseEntity<List<NoteDto>> getAllNotesInfo(){//  For test cases only
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes() {// getting All notes(title and date only)
        log.info("Getting (fetching) all notes (title + date only)");
        List<NoteDto> noteSummaryDto = noteService.getAllNotes();
        return ResponseEntity.ok(noteSummaryDto);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<NoteDetailsDto> getNoteDetails(@PathVariable String id) {//   Note details contain only text and tags(optionally)
        log.info("Fetching note details for id: {}", id);
        return noteService.getNoteDetailsDTOById(id)// TODO разобраться с возвращением dto
                .<ResponseEntity<NoteDetailsDto>>
                        map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Long>> noteStats(@PathVariable String id){//
        log.info("Note stats retrieved for id: {}", id);
        return ResponseEntity.ok(noteService.getWordStats(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<NoteDto>> getNotesByTag(@RequestParam Tags tag) {
        log.info("Filtering notes by tag: {}", tag);
        return ResponseEntity.ok(noteService.getNotesByTag(tag));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<NoteDto>> getAllNotesSorted() {
        log.info("Fetching all notes sorted by createdDate (DESC)");
        return ResponseEntity.ok(noteService.getAllNotesSorted());
    }

    @GetMapping("/page")
    public ResponseEntity<List<NoteDto>> getNotesPage(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        log.info("Fetching notes page: {}, size: {}", page, size);
        return ResponseEntity.ok(noteService.getNotesPage(pageable));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNote(@PathVariable String id, @Valid @RequestBody Note note){
        noteService.updateNote(id, note);
        log.info("Note successfully updated!");
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id){
        noteService.deleteNoteById(id);
        log.info("Note successfully deleted!");
        return ResponseEntity.noContent().build();
    }
}

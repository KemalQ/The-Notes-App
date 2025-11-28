package com.example.notesApp.controller;

import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteSummaryDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
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
    private final NoteServiceImpl noteService;

    public NotesController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity createNote(@Valid @RequestBody Note note){// creating note
        log.info("Getting request for creating the note");

        // Step 1. Saving entity in DB and Create entity variable
        Note savedNote = noteService.createNote(note);

        // Step 2. Creating DTO object to return in
        NoteSummaryDto noteDetailsDTO = new NoteSummaryDto(
                savedNote.getTitle(),
                savedNote.getCreatedDate());

        // Step 3. Creating URI location for saved entity (NOT DTO!)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNote.getId())
                .toUri();

        // Step 4. Returning status code and DTO in body, setting HTTP Header location
        // (ResponseEntity: HTTP Status Code, request body, HTTP Header)
        return ResponseEntity.created(location).body(noteDetailsDTO);
    }

    @GetMapping("/fullinfo")
    public List<NoteSummaryDto> getAllNotesInfo(){//  For tests only
        return noteService.getAllNotes();
    }

    @GetMapping
    public ResponseEntity<List<NoteSummaryDto>> getAllNotes() {// getting All notes(title and date only)
        log.info("Getting (fetching) all notes (title + date only)");
        List<NoteSummaryDto> noteSummaryDto = noteService.getAllNotes();
        return ResponseEntity.ok(noteSummaryDto);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<NoteDetailsDto> getNoteDetails(@PathVariable String id) {//   Note details contain only text and tags(optionally)
        log.info("Fetching note details for id: {}", id);
        return noteService.getNoteDetailsDTOById(id)
                .<ResponseEntity<NoteDetailsDto>>
                        map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/stats")
    public Map<String, Long> noteStats(@PathVariable String id){//
        log.info("Note stats retrieved for id: {}", id);
        return noteService.getWordStats(id);
    }

    @GetMapping("/filter")
    public List<NoteSummaryDto> getNotesByTag(@RequestParam Tags tag) {
        log.info("Filtering notes by tag: {}", tag);
        return noteService.getNotesByTag(tag).stream().map(
                note -> new NoteSummaryDto(note.getTitle(), note.getCreatedDate())).toList();
    }

    @GetMapping("/sorted")
    public List<NoteSummaryDto> getAllNotesSorted() {
        log.info("Fetching all notes sorted by createdDate (DESC)");
        return noteService.getAllNotesSorted().stream().map(
                note -> new NoteSummaryDto(note.getTitle(), note.getCreatedDate())).toList();
    }

    @GetMapping("/page")
    public List<NoteSummaryDto> getNotesPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        log.info("Fetching notes page: {}, size: {}", page, size);
        return noteService.getNotesPage(pageable).stream().map(
                note -> new NoteSummaryDto(note.getTitle(), note.getCreatedDate())).toList();
    }


    @PutMapping("/{id}")
    public void updateNote(@PathVariable String id, @Valid @RequestBody Note note){
        noteService.updateNote(id, note);
        log.info("Note successfully updated!");
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id){
        noteService.deleteNoteById(id);
        log.info("Note successfully deleted!");
    }
}

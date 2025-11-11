package com.example.notesApp.controller;

import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import com.example.notesApp.service.NoteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    public void createNote(@Valid @RequestBody Note note){
        noteService.createNote(note);
        log.info("Note successfully created!");
    }

    @GetMapping
    public List<Note> getAllNotes(){
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable String id){
        return noteService.getNoteById(id);
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

    @GetMapping("/{id}/stats")
    public Map<String, Long> noteStats(@PathVariable String id){
        log.info("Note stats retrieved for id: {}", id);
        return noteService.getWordStats(id);
    }

    @GetMapping("/filter")
    public List<Note> getNotesByTag(@RequestParam Tags tag) {
        return noteService.getNotesByTag(tag);
    }

    @GetMapping("/sorted")
    public List<Note> getAllNotesSorted() {
        return noteService.getAllNotesSorted();
    }

    @GetMapping("/page")
    public List<Note> getNotesPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        return noteService.getNotesPage(pageable);
    }


    @GetMapping("/{id}/text")
    public String getNoteText(@PathVariable String id) {
        return noteService.getNoteById(id).getText();
    }
}

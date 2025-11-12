package com.example.notesApp.service;

import com.example.notesApp.dao.NotesDAO;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Slf4j
@Service
public class NoteService {
    private final NotesDAO notesDAO;
    private final NoteStatisticService noteStatistic;

    public NoteService(NotesDAO notesDAO, NoteStatisticService noteStatistic){
        this.notesDAO = notesDAO;
        this.noteStatistic = noteStatistic;
    }

    public void createNote(Note note){
        note.setCreatedDate(new Date());// устанавливаю текущую дату на новый note
        notesDAO.save(note);
        log.info("Note successfully saved");
    }

    public void updateNote(String id, Note note){
        Note existing = notesDAO.findById(id)
                        .orElseThrow(()-> new NoSuchElementException("Note not found with id: " + id));
        existing.setTitle(note.getTitle());
        existing.setText(note.getText());
        existing.setTags(note.getTags());
        notesDAO.save(existing);
        log.info("{} id note updated", id);
    }

    public void deleteNoteById(String id){
        notesDAO.deleteById(id);
        log.info("Note in id = {} deleted", id);
    }

    public List<Note> getAllNotes(){
        return notesDAO.findAll();
    }

    public Note getNoteById(String id){
        return notesDAO.findById(id)
                .orElseThrow( ()-> new NoSuchElementException("Note not found with id: " + id));
    }

    public Map<String, Long> getWordStats(String id){
        Note note = getNoteById(id);
        return noteStatistic.calculateWordStatistics(note.getText());
    }

    public List<Note> getAllNotesSorted() {
        return notesDAO.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    public List<Note> getNotesByTag(Tags tag) {
        return notesDAO.findByTags(tag);
    }

    public List<Note> getNotesPage(Pageable pageable) {
        return notesDAO.findAll(pageable).getContent();
    }
}

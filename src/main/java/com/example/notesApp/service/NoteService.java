package com.example.notesApp.service;

import com.example.notesApp.dao.NotesDAO;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteSummaryDto;
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

    public Note createNote(Note note){
        note.setCreatedDate(new Date());// Setting current time
        Note savedNote = notesDAO.save(note);
        log.info("Note successfully saved");
        return savedNote;
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

    public List<NoteSummaryDto> getAllNotes(){
        return notesDAO.findAll().stream()
                .map(note -> new NoteSummaryDto(note.getTitle(), note.getCreatedDate())).toList();
    }

    public Optional<NoteDetailsDto> getNoteDetailsDTOById(String id){
        return notesDAO.findById(id).map(
                note -> new NoteDetailsDto(
                        note.getText(),
                        note.getTags())
        );
    }

    public Map<String, Long> getWordStats(String id){
        Note note = notesDAO.findById(id).orElseThrow();// TODO check
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

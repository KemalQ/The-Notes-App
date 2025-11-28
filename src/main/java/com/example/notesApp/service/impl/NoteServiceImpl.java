package com.example.notesApp.service.impl;

import com.example.notesApp.dao.NotesDAO;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.mapper.NoteMapper;
import com.example.notesApp.model.Note;
import com.example.notesApp.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService {
    private final NotesDAO notesDAO;
    private final NoteStatisticService noteStatistic;
    private final NoteMapper noteMapper;

    public NoteServiceImpl(NotesDAO notesDAO, NoteStatisticService noteStatistic, NoteMapper noteMapper){
        this.notesDAO = notesDAO;
        this.noteStatistic = noteStatistic;
        this.noteMapper = noteMapper;
    }


    public Note createNote(Note note){
        note.setCreatedDate(new Date());
        log.info("Note successfully saved");
        return notesDAO.save(note);
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
        if (notesDAO.existsById(id) == false){
            throw new NoSuchElementException("Note not found with id " + id);
        }
        notesDAO.deleteById(id);
        log.info("Note in id = {} deleted", id);
    }

    public List<NoteDto> getAllNotes(){
        return notesDAO.findAll().stream()
                .map(noteMapper::toNoteDto).toList();
    }

    public List<NoteDto> getAllNotesSorted() {
        return notesDAO.findAll(Sort.by(Sort.Direction.DESC, "createdDate")).stream()
                .map(noteMapper::toNoteDto).toList();
    }

    public List<NoteDto> getNotesByTag(Tags tag) {
        return notesDAO.findByTags(tag).stream().map(noteMapper::toNoteDto).toList();
    }

    public List<NoteDto> getNotesPage(Pageable pageable) {
        return notesDAO.findAll(pageable).getContent().stream()
                .map(noteMapper::toNoteDto).toList();
    }

    public Optional<NoteDetailsDto> getNoteDetailsDTOById(String id){
        return notesDAO.findById(id).map(noteMapper::toNoteDetailsDto);
    }

    public Map<String, Long> getWordStats(String id){
        Note note = notesDAO.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Note not found with id: " + id));// TODO check
        return noteStatistic.calculateWordStatistics(note.getText());
    }

}

package com.example.notesApp.service.impl;

import com.example.notesApp.dao.NotesDAO;
import com.example.notesApp.dto.CreateNoteDto;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.dto.PutNoteDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.exceptions.NoteNotFoundException;
import com.example.notesApp.mapper.NoteMapper;
import com.example.notesApp.model.Note;
import com.example.notesApp.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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

    @Override
    public NoteDto createNote(CreateNoteDto noteDto){
        Note note = noteMapper.toNote(noteDto);
        NoteDto savedNote = noteMapper.toNoteDto(notesDAO.save(note));
        log.info("Note successfully saved");
        return savedNote;
    }

    @Override
    public void updateNote(String id, PutNoteDto putNoteDto){
        Note existing = notesDAO.findById(id)
                        .orElseThrow(()-> new NoteNotFoundException("Note not found with id: " + id));
        noteMapper.updateNoteFromDto(putNoteDto, existing);
        notesDAO.save(existing);
        log.info("{} id note updated", id);
    }

    @Override
    public void deleteNoteById(String id){
        if (!notesDAO.existsById(id)){
            throw new NoteNotFoundException("Note not found with id " + id);
        }
        notesDAO.deleteById(id);
        log.info("Note in id = {} deleted", id);
    }

    @Override
    public List<NoteDto> getAllNotes(){
        return notesDAO.findAll().stream()
                .map(noteMapper::toNoteDto).toList();
    }

    @Override
    public List<NoteDto> getAllNotesSorted() {
        return notesDAO.findAll(Sort.by(Sort.Direction.DESC, "createdDate")).stream()
                .map(noteMapper::toNoteDto).toList();
    }

    @Override
    public List<NoteDto> getNotesByTag(Tags tag) {
        return notesDAO.findByTags(tag).stream().map(noteMapper::toNoteDto).toList();
    }

    @Override
    public List<NoteDto> getNotesPage(Pageable pageable) {
        return notesDAO.findAll(pageable).getContent().stream()
                .map(noteMapper::toNoteDto).toList();
    }

    @Override
    public NoteDetailsDto getNoteDetailsDTOById(String id){
        return notesDAO.findById(id).map(noteMapper::toNoteDetailsDto)
                .orElseThrow(()-> new NoteNotFoundException("Note not found with id: " + id));
    }

    @Override
    public Map<String, Long> getWordStats(String id){
        Note note = notesDAO.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + id));// TODO check
        return noteStatistic.calculateWordStatistics(note.getText());
    }
}

// TODO остановился на строке 36 fix issue with LocalDateTime
// Потом нужно добавить класс Exception и настроить его работу
// Далее по Claude
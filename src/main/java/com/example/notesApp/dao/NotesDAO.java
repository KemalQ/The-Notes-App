package com.example.notesApp.dao;

import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotesDAO extends MongoRepository<Note, String> {
    public List<Note> findByTags(Tags tags);
}

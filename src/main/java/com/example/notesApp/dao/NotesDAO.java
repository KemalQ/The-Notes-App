package com.example.notesApp.dao;

import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface NotesDAO extends MongoRepository<Note, String> {
    public List<Note> findByTags(Tags tags);

    @Query("""
            SELECT n FROM Note
            WHERE 
                LOWER(n.title) LIKE LOWER(CONCAT('%', :text, '%')) OR
                LOWER(n.tags) LIKE LOWER(CONCAT('%', :text, '%'))
            """)
    public List<Note> searchAll(String text);
}

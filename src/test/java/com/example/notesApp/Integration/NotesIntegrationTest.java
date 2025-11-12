package com.example.notesApp.Integration;

import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import com.example.notesApp.dao.NotesDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotesDAO notesDAO;

    @AfterEach
    void cleanup() {
        notesDAO.deleteAll();
    }

    @Test
    void createAndGetNote() throws Exception {
        Note note = new Note();
        note.setTitle("Integration Test");
        note.setText("hello world hello");
        note.setTags(List.of(Tags.PERSONAL));
        note.setCreatedDate(new Date());

        // Создание
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration Test"));

        Note saved = notesDAO.findAll().get(0);
        mockMvc.perform(get("/api/notes/" + saved.getId() + "/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("hello world hello"))
                .andExpect(jsonPath("$.tags[0]").value("PERSONAL"));

        mockMvc.perform(get("/api/notes/" + saved.getId() + "/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hello").value(2))
                .andExpect(jsonPath("$.world").value(1));
    }

    @Test
    void updateAndDeleteNote() throws Exception {
        Note note = new Note();
        note.setTitle("Old Title");
        note.setText("Old text");
        note.setCreatedDate(new Date());
        notesDAO.save(note);

        Note saved = notesDAO.findAll().get(0);

        // Update
        Note update = new Note();
        update.setTitle("New Title");
        update.setText("New text");
        update.setTags(List.of(Tags.IMPORTANT));

        mockMvc.perform(put("/api/notes/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/notes/" + saved.getId() + "/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New text"))
                .andExpect(jsonPath("$.tags[0]").value("IMPORTANT"));

        // Delete
        mockMvc.perform(delete("/api/notes/" + saved.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void filterNotesByTag() throws Exception {
        Note n1 = new Note(null, "A", new Date(), "Text A", List.of(Tags.BUSINESS));
        Note n2 = new Note(null, "B", new Date(), "Text B", List.of(Tags.PERSONAL));
        notesDAO.saveAll(List.of(n1, n2));

        mockMvc.perform(get("/api/notes/filter")
                        .param("tag", "BUSINESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("A"))
                .andExpect(jsonPath("$", hasSize(1)));
    }
}

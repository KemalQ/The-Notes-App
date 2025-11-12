package com.example.notesApp.controller;

import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteSummaryDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import com.example.notesApp.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NotesController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientWebSecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration.class
        })
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService; // зависимость контроллера

    @Test
    void createNote_shouldReturnOk() throws Exception {
        Note note = new Note();
        note.setTitle("Test");
        note.setText("note is just a note");

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk());

        verify(noteService).createNote(any(Note.class));
    }

    @Test
    void getAllNotes_shouldReturnNoteSummaries() throws Exception {
        Note n1 = new Note(); n1.setTitle("A"); n1.setCreatedDate(new Date());
        Note n2 = new Note(); n2.setTitle("B"); n2.setCreatedDate(new Date());
        when(noteService.getAllNotes()).thenReturn(List.of(n1, n2));

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getNoteDetails_shouldReturnDetailsDto() throws Exception {
        Note note = new Note();
        note.setText("Some text");
        note.setTags(List.of(Tags.PERSONAL));
        when(noteService.getNoteById("69137b0bcc2228150a5a4bb4")).thenReturn(note);

        mockMvc.perform(get("/api/notes/69137b0bcc2228150a5a4bb4/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Some text"))
                .andExpect(jsonPath("$.tags[0]").value("PERSONAL"));
    }

    @Test
    void getWordStats_shouldReturnMap() throws Exception {
        when(noteService.getWordStats("1")).thenReturn(Map.of("hello", 2L, "world", 1L));

        mockMvc.perform(get("/api/notes/691380f865d7189ce1c0005b/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hello").value(2))
                .andExpect(jsonPath("$.world").value(1));
    }

    @Test
    void updateNote_shouldCallService() throws Exception {
        Note note = new Note();
        note.setTitle("Updated"); note.setText("New text");

        mockMvc.perform(put("/api/notes/69137a7ccc2228150a5a4bb3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk());

        verify(noteService).updateNote(eq("69137a7ccc2228150a5a4bb3"), any(Note.class));
    }

    @Test
    void deleteNote_shouldCallService() throws Exception {
        mockMvc.perform(delete("/api/notes/69137a7ccc2228150a5a4bb3"))
                .andExpect(status().isOk());

        verify(noteService).deleteNoteById("69137a7ccc2228150a5a4bb3");
    }
}

package com.example.notesApp.controller;

import com.example.notesApp.dto.CreateNoteDto;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.dto.PutNoteDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.exceptions.GlobalExceptionHandler;
import com.example.notesApp.exceptions.NoteNotFoundException;
import com.example.notesApp.mapper.NoteMapper;
import com.example.notesApp.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = NotesController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
@Import(GlobalExceptionHandler.class)
class NotesControllerWebMvcTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean NoteService noteService;

    @Test
    void createNote_shouldReturn201_andBody() throws Exception {
        // given
        CreateNoteDto dto = new CreateNoteDto();
        dto.setTitle("Title");
        dto.setText("Text");
        dto.setTags(List.of(Tags.BUSINESS));

        NoteDto response = new NoteDto("Title", LocalDateTime.of(2026, 1, 21, 12, 0));
        when(noteService.createNote(any(CreateNoteDto.class))).thenReturn(response);

        // when + then
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.createdDate").exists());

        // verify: вызов сервиса и полезная проверка входных данных
        ArgumentCaptor<CreateNoteDto> captor = ArgumentCaptor.forClass(CreateNoteDto.class);
        verify(noteService, times(1)).createNote(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("Title");
        assertThat(captor.getValue().getText()).isEqualTo("Text");
        assertThat(captor.getValue().getTags()).containsExactly(Tags.BUSINESS);

        verifyNoMoreInteractions(noteService);
    }

    @Test
    void createNote_shouldReturn400_whenValidationFails() throws Exception {
        // given: заведомо невалидно (как у тебя)
        CreateNoteDto dto = new CreateNoteDto();
        dto.setTitle("");
        dto.setText("");

        // when + then
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/notes"))
                .andExpect(jsonPath("$.errorTime").exists());

        verifyNoInteractions(noteService);
    }

    @Test
    void getNoteDetails_shouldReturn200() throws Exception {
        // given
        String id = "123";
        NoteDetailsDto details = new NoteDetailsDto("Hello", List.of(Tags.IMPORTANT));
        when(noteService.getNoteDetailsDTOById(id)).thenReturn(details);

        // when + then
        mockMvc.perform(get("/api/notes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("Hello"))
                .andExpect(jsonPath("$.tags[0]").value("IMPORTANT"));

        verify(noteService).getNoteDetailsDTOById(id);
        verifyNoMoreInteractions(noteService);
    }

    @Test
    void getNoteDetails_shouldReturn404_whenNotFound() throws Exception {
        // given
        String id = "404";
        when(noteService.getNoteDetailsDTOById(id))
                .thenThrow(new NoteNotFoundException("Note not found with id: " + id));

        // when + then
        mockMvc.perform(get("/api/notes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Note not found with id: " + id))
                .andExpect(jsonPath("$.path").value("/api/notes/" + id))
                .andExpect(jsonPath("$.errorTime").exists());

        verify(noteService).getNoteDetailsDTOById(id);
        verifyNoMoreInteractions(noteService);
    }

    @Test
    void noteStats_shouldReturn200() throws Exception {
        // given
        String id = "123";
        when(noteService.getWordStats(id)).thenReturn(Map.of("java", 2L));

        // when + then
        mockMvc.perform(get("/api/notes/{id}/stats", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.java").value(2));

        verify(noteService).getWordStats(id);
        verifyNoMoreInteractions(noteService);
    }

    @Test
    void updateNote_shouldReturn204() throws Exception {
        // given
        String id = "123";
        PutNoteDto dto = new PutNoteDto();
        dto.setTitle("New");
        dto.setText("New text");
        dto.setTags(List.of(Tags.PERSONAL));

        // when + then
        mockMvc.perform(put("/api/notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // 204 обычно без тела

        ArgumentCaptor<PutNoteDto> captor = ArgumentCaptor.forClass(PutNoteDto.class);
        verify(noteService).updateNote(eq(id), captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("New");
        assertThat(captor.getValue().getText()).isEqualTo("New text");
        assertThat(captor.getValue().getTags()).containsExactly(Tags.PERSONAL);

        verifyNoMoreInteractions(noteService);
    }

    @Test
    void deleteNote_shouldReturn204() throws Exception {
        // given
        String id = "123";

        // when + then
        mockMvc.perform(delete("/api/notes/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(noteService).deleteNoteById(id);
        verifyNoMoreInteractions(noteService);
    }
}

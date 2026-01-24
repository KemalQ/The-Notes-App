package com.example.notesApp.service.impl;

import com.example.notesApp.dao.NotesDAO;
import com.example.notesApp.dto.*;
import com.example.notesApp.exceptions.NoteNotFoundException;
import com.example.notesApp.mapper.NoteMapper;
import com.example.notesApp.model.Note;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTest {
    @Mock
    private NotesDAO notesDAO;
    @Mock
    private NoteStatisticService noteStatisticService;
    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    void createNote_shouldMapSaveAndReturnResponseDto() {
        // given
        CreateNoteDto createDto = new CreateNoteDto();
        createDto.setTitle("Test");
        createDto.setText("Some text");

        Note noteEntity = new Note();

        Note savedEntity = new Note();

        CreateNoteResponseDto responseDto = new CreateNoteResponseDto();
        responseDto.setId("69742fc38386874d1ad74118");
        responseDto.setTitle("Test");
        responseDto.setCreatedDate(LocalDateTime.now());

        when(noteMapper.toNote(createDto)).thenReturn(noteEntity);
        when(notesDAO.save(noteEntity)).thenReturn(savedEntity);
        when(noteMapper.toCreatedNoteDto(savedEntity)).thenReturn(responseDto);

        // when
        CreateNoteResponseDto result = noteService.createNote(createDto);

        // then
        assertNotNull(result);
        assertEquals("Test", result.getTitle());
        assertEquals("69742fc38386874d1ad74118", result.getId());

        verify(noteMapper).toNote(createDto);
        verify(notesDAO).save(noteEntity);
        verify(noteMapper).toCreatedNoteDto(savedEntity);
        verifyNoMoreInteractions(notesDAO, noteMapper);
        verifyNoInteractions(noteStatisticService);
    }

    @Test
    void updateNote_shouldUpdateExistingNote() {
        String noteId = "123";
        PutNoteDto putDto = new PutNoteDto();
        putDto.setTitle("Updated");
        putDto.setText("Updated text");

        Note existingNote = new Note();
        existingNote.setId(noteId);

        when(notesDAO.findById(noteId)).thenReturn(Optional.of(existingNote));

        //when
        noteService.updateNote(noteId, putDto);

        //then
        verify(notesDAO).findById(noteId);
        verify(noteMapper).updateNoteFromDto(putDto, existingNote);
        verify(notesDAO).save(existingNote);
    }

    @Test
    void updateNote_shouldThrowException_whenNoteNotFound() {
        //given
        String noteId = "404";
        PutNoteDto putDto = new PutNoteDto();

        when(notesDAO.findById(noteId)).thenReturn(Optional.empty());

        //then
        assertThrows(NoteNotFoundException.class,
                () -> noteService.updateNote(noteId, putDto));

        verify(notesDAO).findById(noteId);
        verifyNoMoreInteractions(notesDAO, noteMapper);
    }


    @Test
    void deleteNoteById_shouldDeleteNote_whenExists(){
        String id = "123";
        when(notesDAO.existsById(id)).thenReturn(true);

        noteService.deleteNoteById(id);

        verify(notesDAO).existsById(id);
        verify(notesDAO).deleteById(id);
        verifyNoMoreInteractions(notesDAO);
    }

    @Test
    void deleteNoteById_shouldThrowException_whenNoteNotFound() {
        String noteId = "404";
        when(notesDAO.existsById(noteId)).thenReturn(false);

        assertThrows(NoteNotFoundException.class,
                () -> noteService.deleteNoteById(noteId));

        verify(notesDAO).existsById(noteId);
        verifyNoMoreInteractions(notesDAO);
    }

    @Test
    void getNoteDetailsDTOById_shouldReturnNoteDetails() {
        String noteId = "123";
        Note note = new Note();
        NoteDetailsDto detailsDto = new NoteDetailsDto("text", List.of());

        when(notesDAO.findById(noteId)).thenReturn(Optional.of(note));
        when(noteMapper.toNoteDetailsDto(note)).thenReturn(detailsDto);

        //  when
        NoteDetailsDto result = noteService.getNoteDetailsDTOById(noteId);

        //   then
        assertNotNull(result);
        verify(notesDAO).findById(noteId);
        verify(noteMapper).toNoteDetailsDto(note);
    }

    @Test
    void getNoteDetailsDTOById_shouldThrowException_whenNotFound() {
        String noteId = "404";
        when(notesDAO.findById(noteId)).thenReturn(Optional.empty());

        //then
        assertThrows(NoteNotFoundException.class,
                () -> noteService.getNoteDetailsDTOById(noteId));

        verify(notesDAO).findById(noteId);
        verifyNoMoreInteractions(notesDAO, noteMapper);
    }

    @Test
    void getWordStats_shouldReturnStatistics() {
        String noteId = "123";
        Note note = new Note();
        note.setText("java java spring");

        Map<String, Long> stats = Map.of("java", 2L, "spring", 1L);

        when(notesDAO.findById(noteId)).thenReturn(Optional.of(note));
        when(noteStatisticService.calculateWordStatistics(note.getText()))
                .thenReturn(stats);

        //when
        Map<String, Long> result = noteService.getWordStats(noteId);

        //then
        assertEquals(2L, result.get("java"));
        verify(notesDAO).findById(noteId);
        verify(noteStatisticService).calculateWordStatistics(note.getText());
    }

    @Test
    void getWordStats_shouldThrowException_whenNoteNotFound() {
        //given
        String noteId = "404";
        when(notesDAO.findById(noteId)).thenReturn(Optional.empty());

        //      then
        assertThrows(NoteNotFoundException.class,
                () -> noteService.getWordStats(noteId));

        verify(notesDAO).findById(noteId);
        verifyNoMoreInteractions(notesDAO, noteStatisticService);
    }

}

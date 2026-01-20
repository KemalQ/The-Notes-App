package com.example.notesApp.service.impl;

import com.example.notesApp.dao.NotesDAO;
import com.example.notesApp.dto.CreateNoteDto;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.dto.PutNoteDto;
import com.example.notesApp.exceptions.NoteNotFoundException;
import com.example.notesApp.mapper.NoteMapper;
import com.example.notesApp.model.Note;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void createNote_shouldSaveNoteAndReturnNoteDto() {
        CreateNoteDto createDto = new CreateNoteDto();
        createDto.setTitle("Test");
        createDto.setText("Some text");

        Note noteEntity = new Note();
        NoteDto noteDto = new NoteDto("Test", null);

        when(noteMapper.toNote(createDto)).thenReturn(noteEntity);
        when(notesDAO.save(noteEntity)).thenReturn(noteEntity);
        when(noteMapper.toNoteDto(noteEntity)).thenReturn(noteDto);

        NoteDto result = noteService.createNote(createDto);

        assertNotNull(result);
    }

    @Test
    void createNote_shouldMapSaveAndReturnDto() {
        CreateNoteDto createDto = new CreateNoteDto();
        createDto.setTitle("Test");
        createDto.setText("Some text");

        Note noteEntity = new Note();
        NoteDto noteDto = new NoteDto("Test", null);

        when(noteMapper.toNote(createDto)).thenReturn(noteEntity);
        when(notesDAO.save(noteEntity)).thenReturn(noteEntity);
        when(noteMapper.toNoteDto(noteEntity)).thenReturn(noteDto);

        NoteDto result = noteService.createNote(createDto);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());

        verify(noteMapper, times(1)).toNote(createDto);
        verify(notesDAO, times(1)).save(noteEntity);
        verify(noteMapper, times(1)).toNoteDto(noteEntity);
        verifyNoMoreInteractions(notesDAO, noteMapper);
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

package com.example.notesApp.service;

import com.example.notesApp.dao.NotesDAO;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NotesDAO notesDAO;

    @Mock
    private NoteStatisticService noteStatistic;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNote_shouldSetCreatedDateAndSave() {
        Note note = new Note();
        note.setTitle("Test");
        note.setText("Some text");

        noteService.createNote(note);

        assertNotNull(note.getCreatedDate());
        verify(notesDAO, times(1)).save(note);
    }

    @Test
    void updateNote_shouldUpdateFields() {
        Note existing = new Note();
        existing.setId("69137a7ccc2228150a5a4bb3");
        existing.setTitle("Old");
        existing.setText("Old text");

        Note updated = new Note();
        updated.setTitle("New");
        updated.setText("New text");

        when(notesDAO.findById("69137a7ccc2228150a5a4bb3")).thenReturn(Optional.of(existing));

        noteService.updateNote("69137a7ccc2228150a5a4bb3", updated);

        assertEquals("New", existing.getTitle());
        assertEquals("New text", existing.getText());
        verify(notesDAO).save(existing);
    }

    @Test
    void updateNote_shouldThrowIfNotFound() {
        when(notesDAO.findById("notfound")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> noteService.updateNote("notfound", new Note()));
    }

    @Test
    void deleteNote_shouldCallDeleteById() {
        noteService.deleteNoteById("69137a7ccc2228150a5a4bb3");
        verify(notesDAO).deleteById("69137a7ccc2228150a5a4bb3");
    }

    @Test
    void getAllNotes_shouldReturnList() {
        List<Note> notes = List.of(new Note(), new Note());
        when(notesDAO.findAll()).thenReturn(notes);

        List<Note> result = noteService.getAllNotes();

        assertEquals(2, result.size());
        verify(notesDAO).findAll();
    }

    @Test
    void getNoteById_shouldReturnNote() {
        Note note = new Note();
        when(notesDAO.findById("69137a7ccc2228150a5a4bb3")).thenReturn(Optional.of(note));

        Note result = noteService.getNoteById("69137a7ccc2228150a5a4bb3");

        assertEquals(note, result);
    }

    @Test
    void getWordStats_shouldReturnStatisticsFromService() {
        Note note = new Note();
        note.setId("1");
        note.setText("note is just a note");
        when(notesDAO.findById("1")).thenReturn(Optional.of(note));
        when(noteStatistic.calculateWordStatistics("note is just a note"))
                .thenReturn(Map.of("note", 2L, "is", 1L, "just",1L, "a", 1L));

        Map<String, Long> stats = noteService.getWordStats("1");

        assertEquals(2L, stats.get("note"));
        assertEquals(1L, stats.get("just"));
    }

    @Test
    void getAllNotesSorted_shouldCallFindAllWithSort() {
        noteService.getAllNotesSorted();
        verify(notesDAO).findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    @Test
    void getNotesByTag_shouldCallFindByTags() {
        noteService.getNotesByTag(Tags.IMPORTANT);
        verify(notesDAO).findByTags(Tags.IMPORTANT);
    }

    @Test
    void getNotesPage_shouldCallFindAllWithPageable() {
        PageRequest pageable = PageRequest.of(0, 5);
        when(notesDAO.findAll(pageable)).thenReturn(new PageImpl<>(List.of(new Note())));
        List<Note> result = noteService.getNotesPage(pageable);

        assertEquals(1, result.size());
        verify(notesDAO).findAll(pageable);
    }
}

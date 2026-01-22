package com.example.notesApp.mapper;

import com.example.notesApp.dto.CreateNoteDto;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.dto.PutNoteDto;
import com.example.notesApp.enums.Tags;
import com.example.notesApp.model.Note;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoteMapperTest {
    private final NoteMapper noteMapper = Mappers.getMapper(NoteMapper.class);

    @Test
    void toNote_shouldMapCreateNoteDtoToNote(){
        CreateNoteDto dto = new CreateNoteDto();
        dto.setText("Text");
        dto.setTitle("Title");
        dto.setTags(List.of(Tags.PERSONAL));

        Note note = noteMapper.toNote(dto);

        assertEquals("Text", note.getText());
        assertEquals("Title", note.getTitle());
        assertEquals(List.of(Tags.PERSONAL), note.getTags());
    }

    @Test
    void toNoteDto_shouldMapNoteToNoteDto() {
        LocalDateTime created = LocalDateTime.of(2026, 1, 20, 12, 0);

        Note note = new Note();
        note.setId("1");
        note.setTitle("Title");
        note.setCreatedDate(created);
        note.setText("Text");
        note.setTags(List.of(Tags.PERSONAL));

        NoteDto dto = noteMapper.toNoteDto(note);

        assertEquals("Title", dto.getTitle());
        assertEquals(created, dto.getCreatedDate());
    }

    @Test
    void toNote_shouldMapCreateNoteToNoteDetailsDto(){
        Note note = new Note();

        note.setText("Text");
        note.setTags(List.of(Tags.BUSINESS, Tags.IMPORTANT));

        NoteDetailsDto dto = noteMapper.toNoteDetailsDto(note);

        assertEquals("Text", dto.getText());
        assertEquals(List.of(Tags.BUSINESS, Tags.IMPORTANT), dto.getTags());
    }

    @Test
    void updateNoteFromDto_shouldUpdateExistingNote(){
        PutNoteDto putNoteDto = new PutNoteDto();
        putNoteDto.setTitle("New Title");
        putNoteDto.setText("New Text");
        putNoteDto.setTags(new ArrayList<>(List.of(Tags.PERSONAL)));

        Note note = new Note();
        note.setId("123");
        note.setTitle("Old Title");
        note.setText("Old Text");
        note.setTags(new ArrayList<>(List.of(Tags.BUSINESS)));

        noteMapper.updateNoteFromDto(putNoteDto, note);

        assertEquals("New Text", note.getText());
        assertEquals("New Title", note.getTitle());
        assertEquals(List.of(Tags.PERSONAL), note.getTags());
        assertEquals("123", note.getId());
    }

}

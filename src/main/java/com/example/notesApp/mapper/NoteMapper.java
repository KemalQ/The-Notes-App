package com.example.notesApp.mapper;

import com.example.notesApp.dto.CreateNoteDto;
import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.dto.PutNoteDto;
import com.example.notesApp.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteDto toNoteDto(Note note);
    NoteDetailsDto toNoteDetailsDto(Note note);

    Note toNote(CreateNoteDto noteDto);

    void updateNoteFromDto(PutNoteDto putNoteDto, @MappingTarget Note note);
}

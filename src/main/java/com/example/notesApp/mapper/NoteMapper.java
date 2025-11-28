package com.example.notesApp.mapper;

import com.example.notesApp.dto.NoteDetailsDto;
import com.example.notesApp.dto.NoteDto;
import com.example.notesApp.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);

    NoteDto toNoteDto(Note note);
    NoteDetailsDto toNoteDetailsDto(Note note);
}

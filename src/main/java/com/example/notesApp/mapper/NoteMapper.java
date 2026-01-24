package com.example.notesApp.mapper;

import com.example.notesApp.dto.*;
import com.example.notesApp.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteDto toNoteDto(Note note);
    NoteDetailsDto toNoteDetailsDto(Note note);

    Note toNote(CreateNoteDto noteDto);

    void updateNoteFromDto(PutNoteDto putNoteDto, @MappingTarget Note note);

    CreateNoteResponseDto toCreatedNoteDto(Note note);
}

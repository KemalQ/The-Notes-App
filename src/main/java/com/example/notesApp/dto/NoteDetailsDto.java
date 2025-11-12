package com.example.notesApp.dto;


import com.example.notesApp.enums.Tags;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class NoteDetailsDto {
    private String text;
    private List<Tags> tags;
}
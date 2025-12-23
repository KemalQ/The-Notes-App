package com.example.notesApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class NoteDto {
    private String title;
    private LocalDateTime createdDate;
}

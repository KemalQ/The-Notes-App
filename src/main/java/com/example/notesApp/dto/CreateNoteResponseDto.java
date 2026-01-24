package com.example.notesApp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateNoteResponseDto {
    String id;
    String title;
    LocalDateTime createdDate;
}

package com.example.notesApp.dto;

import com.example.notesApp.enums.Tags;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class PutNoteDto {

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    private List<Tags> tags;
}


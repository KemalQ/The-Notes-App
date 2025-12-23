package com.example.notesApp.model;

import com.example.notesApp.enums.Tags;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @Id
    private String id;

    private String title;

    @CreatedDate
    private LocalDateTime createdDate;

    private String text;

    private List<Tags> tags;
}

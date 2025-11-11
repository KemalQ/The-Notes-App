package com.example.notesApp.model;

import com.example.notesApp.enums.Tags;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @Id
    private String id;

    @NotBlank
    private String title;

    @CreatedDate
    private Date createdDate;

    @NotBlank
    private String text;

    private List<Tags> tags;
}

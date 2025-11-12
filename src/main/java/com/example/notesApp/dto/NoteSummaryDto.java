package com.example.notesApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;

@AllArgsConstructor
@Getter
public class NoteSummaryDto {
    private String title;
    private Date createdDate;
}

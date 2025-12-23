package com.example.notesApp.exceptions;

public class NoteNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1;

    public NoteNotFoundException(String message){
        super(message);// send to RuntimeException constructor
    }
}

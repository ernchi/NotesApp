package com.example.notesapp;

public final class Constants {
    private Constants() {
        // prevent instantiation
    }

    // used as key for intent extras
    public static final String NOTE_ACTIVITY_RESULT = "result";
    public static final String NOTE_ACTIVITY_NEW_NOTE = "newNote";
    public static final String MAIN_ACTIVITY_EDIT = "isEdit";
    public static final String MAIN_ACTIVITY_NOTE = "note";

    // used as return codes from NoteActivity
    public static final int RESULT_DELETE = 1;
    public static final int RESULT_UPDATE = 2;
    public static final int RESULT_INSERT = 3;
}

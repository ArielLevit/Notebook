package com.ariellevit.notebook;

/**
 * Created by אריאל on 27/08/2016.
 */
public class Note {

    private String title, message;
    private long noteId, dateCreatedMilli;
    private int hide;

    public Note(String title, String message) {
        this.title = title;
        this.message = message;
        this.noteId = 0;
        this.dateCreatedMilli = 0;
    }

    public Note(String title, String message, long noteId, long dateCreatedMilli, int hide) {
        this.title = title;
        this.message = message;
        this.noteId = noteId;
        this.dateCreatedMilli = dateCreatedMilli;
        this.hide = hide;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getDateCreatedMilli() {
        return dateCreatedMilli;
    }

    public long getNoteId() {
        return noteId;
    }

    public int getHide() {
        return hide;
    }

    public String toString(){
        return "ID: " + noteId + "Title:" + title + "Message:" + message + "Date: " + dateCreatedMilli;
    }



}

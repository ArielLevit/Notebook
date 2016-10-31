package com.ariellevit.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;


public class NotebookDbAdapter {

    private static final String DATABASE_NAME = "notebook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOTE_TABLE = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DELETED = "deleted";

    private String[] allColumns = { COLUMN_ID, COLUMN_TITLE, COLUMN_MESSAGE, COLUMN_DATE, COLUMN_DELETED};

    public static final String CREATE_TABLE_NOTE = "create table " + NOTE_TABLE + " ( "
            + COLUMN_ID + " integer primary  key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_MESSAGE + " text not null, "
            + COLUMN_DATE + ");";

    private SQLiteDatabase sqlDB;
    private Context context;
    private NotebookDbHelper notebookDbHelper;

    public NotebookDbAdapter (Context context){
        this.context = context;
    }

    public NotebookDbAdapter open() throws android.database.SQLException{
        notebookDbHelper = new NotebookDbHelper(context);
        sqlDB = notebookDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        notebookDbHelper.close();
    }

    public Note createNote(String title, String message){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_DELETED, 0);
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        long insertId = sqlDB.insert(NOTE_TABLE, null, values);

        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns, COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
    }

//    public boolean deleteNote(long id) {
//        return sqlDB.delete(NOTE_TABLE, COLUMN_ID + "=" + id, null) > 0;
//    }

    public long updateDeleteNote (long idToUpdate){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED, 1);
        return sqlDB.update(NOTE_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }



    public long updateNote (long idToUpdate, String newTitle, String newMessage){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        values.put(COLUMN_MESSAGE, newMessage);
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        return sqlDB.update(NOTE_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }

    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> notes = new ArrayList<Note>();

        //Grab all the information from our database for the notes in it
        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns, null, null, null, null, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
            Note note = cursorToNote(cursor);
            notes.add(note);
        }

        cursor.close();
        return notes;
    }


    private Note cursorToNote (Cursor cursor) {
        Note newNote = new Note ( cursor.getString(1), cursor.getString(2), cursor.getLong(0),
                cursor.getLong(3), cursor.getInt(4));
        return newNote;
    }


    private static class NotebookDbHelper extends SQLiteOpenHelper{

        NotebookDbHelper (Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create note table
             db.execSQL(CREATE_TABLE_NOTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(NotebookDbHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            //Destroy data
            db.execSQL("DROP TABLE IF EXIST " + NOTE_TABLE);
            onCreate(db);

        }
    }

}

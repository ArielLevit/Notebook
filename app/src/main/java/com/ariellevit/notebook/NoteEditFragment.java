package com.ariellevit.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {


    private EditText title, message;
    private AlertDialog confirmDialogObject;

    private boolean newNote = false;
    private long noteId = 0;

    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Grab the bundle of the new note
        Bundle bundle = this.getArguments();
        if (bundle != null){
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA, false);
        }

        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        title = (EditText)fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText)fragmentLayout.findViewById(R.id.editNoteMessage);
        Button savedButton = (Button) fragmentLayout.findViewById(R.id.saveNote);

        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA, ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA);

        buildConfirmDialog();

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialogObject.show();
            }
        });

        return fragmentLayout;
    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState){
//        super.onSaveInstanceState(savedInstanceState);
//
//    }

    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure you want to save the note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Log.d("Save Note", "Note Title: " + title.getText() + "Note Message: " + message.getText());

                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();
                if(newNote){
                dbAdapter.createNote(title.getText() + "", message.getText() + "");

                }else {
                    //otherwise its an old note so update it in our database
                    dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "");
                }

                dbAdapter.close();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do Nothing Here
            }
        });

        confirmDialogObject = confirmBuilder.create();
    }



}

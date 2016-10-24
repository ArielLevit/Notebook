package com.ariellevit.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ARIEL on 28/08/2016.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    public static class ViewHolder{
        TextView title;
        TextView message;
    }
    public NoteAdapter(Context context, ArrayList <Note> notes) {
        super(context,0, notes);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        //Get the item date for this position
        Note note = getItem(position);

        ViewHolder ViewHolder;

        //Check if an existing view is being reused, otherwise inflate a new view from row layout
        if (convertView == null){
            ViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row,parent,false);

            ViewHolder.title = (TextView) convertView.findViewById(R.id.listTitle);
            ViewHolder.message = (TextView) convertView.findViewById(R.id.noteText);

            convertView.setTag(ViewHolder);
        }else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }

        ViewHolder.title.setText(note.getTitle());
        ViewHolder.message.setText(note.getMessage());
//        ViewHolder.description.setVisibility(View.GONE);

        return convertView;
    }


}

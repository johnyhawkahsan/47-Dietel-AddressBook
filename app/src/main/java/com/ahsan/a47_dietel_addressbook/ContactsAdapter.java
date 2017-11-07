//Class ContactsAdapter is a subclass of RecyclerView.Adapter that supplies data to the ContactsFragment’s RecyclerView.

package com.ahsan.a47_dietel_addressbook;

/**
 * Created by ahsan on 10/28/2017.
 */

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahsan.a47_dietel_addressbook.data.DatabaseDescription.Contact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    // interface implemented by ContactsFragment to respond when the user touches an item in the RecyclerView.
    // Each item in the RecyclerView has a click listener that calls the ContactClickListener’s onClick method and passes the selected contact’s Uri.
    // The ContactsFragment then notifies the MainActivity that a contact was selected, so the MainActivity can display the contact in a DetailFragment
    public interface ContactClickListener{
        void onClick(Uri contactUri);
    }

    //constructor for ContactsAdapter
    public ContactsAdapter(ContactClickListener clickListener){
        this.clickListener = clickListener;
    }

    // ContactsAdapter instance variables
    private Cursor cursor = null;
    private final ContactClickListener clickListener;

    // nested subclass of RecyclerView.ViewHolder used to implement the view-holder pattern in the context of a RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView textView;
        private long rowID;

        // constructor - configures a RecyclerView item's ViewHolder
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);

            // attach listener to itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                // executes when the contact in this ViewHolder is clicked
                @Override
                public void onClick(View v) {
                    clickListener.onClick(Contact.buildContactUri(rowID));
                }
            });
        }

        // set the database row ID for the contact in this ViewHolder
        public void setRowID(long rowID){
            this.rowID = rowID;
        }
    }



    // sets up new list item and its ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // we used the predefined layout android.R.layout.simple_list_item_1, which defines a layout containing one TextView named text1.
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);// return current item's ViewHolder
    }

    // sets the text of the list item to display the search tag
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);//uses Cursor method moveToPosition to move to the contact that corresponds to the current RecyclerView item’s position.
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(Contact._ID)));
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)));//look up the column number for the Contact.COLUMN_NAME column, then call Cursor method getString to get the contact’s name.
    }

    // returns the number of items that adapter binds
    @Override
    public int getItemCount() {
        //returns the total number of rows in the Cursor or 0 if Cursor is null.
        return (cursor != null) ? cursor.getCount() : 0;
    }

    // swap this adapter's current Cursor for a new one
    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

}






























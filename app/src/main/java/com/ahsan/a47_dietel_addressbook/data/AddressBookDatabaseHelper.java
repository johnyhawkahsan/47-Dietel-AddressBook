//Class AddressBookDatabaseHelper is a subclass of SQLiteOpenHelper that creates the database and is used to access the database.
//A subclass of SQLiteOpenHelper that creates the database and enables AddressBookContentProvider to access it.
package com.ahsan.a47_dietel_addressbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahsan.a47_dietel_addressbook.data.DatabaseDescription.Contact;//Importing nested class in DatabaseDescription class


public class AddressBookDatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "AddressBook.db";
    private static final int DATABASE_VERSION = 1;

    // constructor- It has 4 arguments: context, dbName, CursorFactory to use, db version number
    public AddressBookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);//3rd argument-the CursorFactory to use—null indicates that you wish to use the default SQLite CursorFactory
    }

    // creates the contacts table when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the contacts table
        final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + Contact.TABLE_NAME + "(" +
                Contact._ID + " integer primary key, " +
                Contact.COLUMN_NAME + " TEXT, " +
                Contact.COLUMN_PHONE + " TEXT, " +
                Contact.COLUMN_EMAIL + " TEXT, " +
                Contact.COLUMN_STREET + " TEXT, " +
                Contact.COLUMN_CITY + " TEXT, " +
                Contact.COLUMN_STATE + " TEXT, " +
                Contact.COLUMN_ZIP + " TEXT);";
        db.execSQL(CREATE_CONTACTS_TABLE);// create the contacts table

    }

    // normally defines how to upgrade the database when the schema changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //EMPTY
    }
}

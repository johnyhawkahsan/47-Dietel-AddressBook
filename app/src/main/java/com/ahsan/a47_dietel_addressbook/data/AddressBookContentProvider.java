/*
Class AddressBookContentProvider is a subclass of ContentProvider that defines how to manipulate the database.
A ContentProvider subclass that defines query, insert, update and delete operations on the database.
To create this class, use New > Other > Content Provider. For URI authorities specify com.ahsan.a47_dietel_addressbook.data and uncheck the Exported checkbox, then click Finish.
Unchecking Exported indicates that this ContentProvider is for use only in this app. The IDE defines a subclass of ContentProvider and overrides its required methods.
In addition, the IDE declares the ContentProvider AndroidManifest.xml as a <provider> element nested in the <application> element.
This is required to register the ContentProvider with the Android operating system—not only for use in this app, but for use in other apps (when the ContentProvider is exported).
 */
package com.ahsan.a47_dietel_addressbook.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.media.UnsupportedSchemeException;
import android.net.Uri;

import com.ahsan.a47_dietel_addressbook.R;
import com.ahsan.a47_dietel_addressbook.data.DatabaseDescription.Contact;//Imported this data class

public class AddressBookContentProvider extends ContentProvider {
    // used to access the database
    private AddressBookDatabaseHelper dbHelper;

    // A ContentProvider uses a UriMatcher to help determine which operation to perform in its query, insert, update and delete methods
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // constants used with UriMatcher to determine operation to perform
    private static final int ONE_CONTACT = 1; // manipulate one contact
    private static final int CONTACTS = 2; // manipulate contacts table

    // static block to configure this ContentProvider's UriMatcher
    static {
        //Uri for Contact with the specified id (#)-where # is a wildcard that matches a string of numeric characters—in this case, the unique primary-key value for one contact in the contacts table.
        //When a Uri matches this format, the UriMatcher returns the constant ONE_CONTACT.
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, Contact.TABLE_NAME + "/#", ONE_CONTACT);

        // Uri for Contacts table-which represents the entire contacts table. When a Uri matches this format, the UriMatcher returns the constant CONTACTS.
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, Contact.TABLE_NAME, CONTACTS);
    }



    // called when the AddressBookContentProvider is created
    @Override
    public boolean onCreate() {
        // create the AddressBookDatabaseHelper
        dbHelper = new AddressBookDatabaseHelper(getContext());
        return true; // ContentProvider successfully created
    }

    // required method: Not used in this app, so we return null
    @Override
    public String getType(Uri uri) {
        return null;
    }

    // query the database-This method receives five arguments: uri—A Uri representing the data to retrieve. projection—A String array representing the specific columns to retrieve. If this argument is null, all columns will be included in the result.
    // selection—A String containing the selection criteria. This is the SQL WHERE clause, specified without the WHERE keyword. If this argument is null, all rows will be included in the result.
    // selectionArgs—A String array containing the arguments used to replace any argument placeholders (?) in the selection String.sortOrder—A String representing the sort order. This is the SQL ORDER BY clause, specified without the ORDER BY keywords.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // create SQLiteQueryBuilder for querying contacts table
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Contact.TABLE_NAME);

        switch (uriMatcher.match(uri)){

            //Select a specific contact from db to display or edit it's details, we put "where" clause method to it.
            case ONE_CONTACT: // contact with specified id will be selected
                queryBuilder.appendWhere(Contact._ID + "=" + uri.getLastPathSegment());//use the SQLiteQueryBuilder’s appendWhere method to add a WHERE clause containing the contact’s ID to the query.Uri method getLastPathSegment returns the last segment in the Uri
                break;

            //all contacts will be selected because there is no WHERE clause
            case CONTACTS:
                break;

            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }

        // execute the query to select one or all contacts - if projection is null, all columns will be selected, if selection is null, all rows will be selected.
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);

        // configure to watch for content changes---indicate that the Cursor should be updated if the data it refers to changes.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // insert a new contact in the database
    @Override
    public Uri insert(Uri uri, ContentValues values) {//A ContentValues object containing key–value pairs in which the column names are the keys and each key’s value is the data to insert in that column.
        Uri newContactUri = null;

        switch (uriMatcher.match(uri)){

            case CONTACTS:
                // insert the new contact--success yields new contact's row id
                long rowId = dbHelper.getWritableDatabase().insert(Contact.TABLE_NAME, null, values);

                // if the contact was inserted, create an appropriate Uri; otherwise, throw an exception
                if (rowId > 0){// SQLite row IDs start at 1
                    newContactUri = Contact.buildContactUri(rowId);

                    // notify observers that the database changed
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else
                    throw new SQLException(getContext().getString(R.string.insert_failed) + uri);
                break;

            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        return newContactUri;
    }


    // update an existing contact in the database
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int numberOfRowsUpdated; // 1 if update successful; 0 otherwise

        switch (uriMatcher.match(uri)){
            case ONE_CONTACT:
                //get from the uri the id of contact to update
                String id = uri.getLastPathSegment();

                // update the contact
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(Contact.TABLE_NAME, values, Contact._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_update_uri) + uri);
        }

        // if changes were made, notify observers that the database changed
        if (numberOfRowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    // delete an existing contact from the database
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri)){
            case ONE_CONTACT:
                // get from the uri the id of contact to update
                String id = uri.getLastPathSegment();

                // delete the contact
                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(Contact.TABLE_NAME, Contact._ID + "=" + id, selectionArgs );
                break;

            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        // notify observers that the database changed
        if (numberOfRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }
}














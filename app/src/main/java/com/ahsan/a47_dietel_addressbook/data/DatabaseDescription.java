//Class DatabaseDescription describes the database’s contacts table.
/*
DatabaseDescription—This class contains public static fields that are used with the app’s ContentProvider and ContentResolver. The nested
Contact class defines static fields for the name of a database table, the Uri used to access that table via the ContentProvider
and the names of the database table’s columns, and a static method for creating a Uri that references a specific contact in the database.
 */
package com.ahsan.a47_dietel_addressbook.data;

/**
 * Created by ahsan on 10/28/2017.
 */

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {

    //ContentProvider's name: typically the package name
    //ContentProvider’s authority—the name that’s supplied to a ContentResolver to locate a ContentProvider.
    public static final String AUTHORITY = "com.ahsan.a47_dietel_addressbook.data";

    // Each Uri that’s used to access a specific ContentProvider begins with "content://" followed by the authority—this is the ContentProvider’s base Uri.Then we use Uri method parse to create the base Uri.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // nested class Contact defines the database’s table name, the table’s Uri for accessing the table via the ContentProvider and the table’s column names.
    public static final class Contact implements BaseColumns{
        public static final String TABLE_NAME = "contacts"; // table's name

        // Uri for the contacts table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // column names for contacts table's columns
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip";

        // creates a Uri for a specific contact-  Method withAppendedId appends a forward slash (/) and a record ID to the end of the Uri in its first argument.
        // For every database table, you’d typically have a class similar to class Contact.
        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        /*
        NOTE: In a database table, each row typically has a primary key that uniquely identifies the row. When working with ListViews and Cursors, this column’s name must be "_id"—
        Android also uses this for the ID column in SQLite database tables. This name is not required for RecyclerViews, but we use it here due to the similarities between ListViews
        and RecyclerViews, and because we’re using Cursors and a SQLite database. Rather than defining this constant directly in class Contact, we implement interface BaseColumns
        (package android.provider;), which defines the constant _ID with the value "_id".
         */
    }



}

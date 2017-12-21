/*
MainActivity—This class manages the app’s Fragments and implements their callback interface methods to respond when a contact is selected,
a new contact is added, or an existing contact is updated or deleted.
 */
package com.ahsan.a47_dietel_addressbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class MainActivity extends AppCompatActivity
        implements ContactsFragment.ContactsFragmentListener,
        DetailFragment.DetailFragmentListener,
        AddEditFragment.AddEditFragmentListener {

    // key for storing a contact's Uri in a Bundle passed to a fragment
    public static final String CONTACT_URI = "contact_uri";

    private ContactsFragment contactsFragment; // We will use this contactsFragment in both Phone and tablet devices.

    // display ContactsFragment when MainActivity first loads
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //NOTE: Small sized devices automatically load "content_main" for small devices, which has only one fragment container whose id is "fragmentContainer".
        //If the R.id.fragmentContainer exists in MainActivity’s layout, then the app is running on a phone. In this case, we create the ContactsFragment, then lines
        //use a FragmentTransaction to add the ContactsFragment to the user interface.
        //If the Activity is being restored after being shut down or recreated from a configuration change,
        //savedInstanceState will not be null. In this case, else statement code get a reference to the existing ContactsFragment.
        //TODO: Confusion--- In book, it's != null, but in code provided, it's == null.
        if (savedInstanceState == null && findViewById(R.id.fragmentContainer) != null){//Creating our fragment from scratch for Phone device

            Log.i("MainActivity", "if (savedInstanceState == null && findViewById(R.id.fragmentContainer) != null)");
            // create ContactsFragment for Phone
            contactsFragment = new ContactsFragment();

            // use a FragmentTransaction to add the ContactsFragment to the user interface
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactsFragment);//fragmentContainer is in phone's "content_main".
            transaction.commit(); // display ContactsFragment
        }

        //if savedInstanceState != null && fragmentContainer is not found, which means it's a tablet, then add contactsFragment to left portion of tablet
        else {
            Log.i("MainActivity", "else (savedInstanceState != null && findViewById(R.id.fragmentContainer) == null)");
            contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.contactsFragment);
        }

    }

    // display DetailFragment for selected contact
    @Override
    public void onContactSelected(Uri contactUri) { //I think this URI comes with implementation method from ContactsFragment where contact is selected.

        Log.i("MainActivity", "Selected contact Uri we received is :" + contactUri.toString());

        if (findViewById(R.id.fragmentContainer)  != null ) // phone
            displayContact(contactUri, R.id.fragmentContainer);
        else {// tablet
            // calls the FragmentManager’s popBackStack method to pop (remove) the top Fragment on the back stack (if there is one)
            getSupportFragmentManager().popBackStack();
            displayContact(contactUri, R.id.rightPaneContainer);

        }
    }

    // display a contact - This method is same for both Phones and tablets,
    private void displayContact(Uri contactUri, int viewID){
        DetailFragment detailFragment = new DetailFragment();

        // specify contact's Uri as an argument to the DetailFragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(CONTACT_URI, contactUri);
        detailFragment.setArguments(arguments);

        // use a FragmentTransaction to display the DetailFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();// causes DetailFragment to display
    }




    // When user presses add button in ContactsFragment, this method is called and implemented here through interface.
    @Override
    public void onAddContact() {
        if (findViewById(R.id.fragmentContainer) != null) //phone
            displayAddEditFragment(R.id.fragmentContainer, null);//Passing null uri, because for new contact, newContactUri will be created
        else // tablet
            displayAddEditFragment(R.id.rightPaneContainer, null);//Passing null uri, because for new contact, newContactUri will be created
    }

    // display fragment for adding a new or editing an existing contact
    private void displayAddEditFragment(int viewID, Uri contactUri){
        AddEditFragment addEditFragment = new AddEditFragment();

        // if editing existing contact, provide contactUri as an argument
        if (contactUri != null){
            Bundle arguments = new Bundle();
            arguments.putParcelable(CONTACT_URI, contactUri);
            addEditFragment.setArguments(arguments);
        }

        // use a FragmentTransaction to display the AddEditFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();// causes AddEditFragment to display


    }

    // return to contact list when displayed contact deleted
    @Override
    public void onContactDeleted() {
        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList(); // refresh contacts
    }

    // display the AddEditFragment to edit an existing contact
    @Override
    public void onEditContact(Uri contactUri) {
        if (findViewById(R.id.fragmentContainer) != null)// phone
            displayAddEditFragment(R.id.fragmentContainer, contactUri);
        else // tablet
            displayAddEditFragment(R.id.rightPaneContainer, contactUri);
    }

    // update GUI after new contact or updated contact saved
    @Override
    public void onAddEditCompleted(Uri contactUri) {
        Log.i("MainActivity", "Uri received from AddEditFragment, contactUri: " + contactUri);

        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList();// refresh contacts

        if (findViewById(R.id.fragmentContainer) == null){// tablet
            // removes top of back stack
            getSupportFragmentManager().popBackStack();

            // on tablet, display contact that was just added or edited
            displayContact(contactUri, R.id.rightPaneContainer);
        }

    }
}














































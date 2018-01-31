
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetdbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    PetdbHelper mDbHelper = new PetdbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


    }

    public void onStart()
    {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        // Create and/or open a database to read from it
        /*
         --------------------------------Database Query Method NOTES----------------

         -->The parameters it takes essentially convert it into an SQLite statement.
         -->projection[]=column names that we want to return from the table
         -->selection-->String which basically is the where clause in SQLite.

         -->    (for ex--"SELECT id,name FROM PETS WHERE "id"=#)...here the selection will be="id=?"
                where ? will be filled up by the selectionArgs[].

         */

        Cursor cursor;
        String projection[]={PetEntry.COLUMN_ID,PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
        PetEntry.COLUMN_PET_WEIGHT};

        /*
        The following was done initially ie directly using query method on the database via the helper.In general this
        is considered a bad practise.
        We use a ContentProvider here.(below the commented part)
         */

     /*  Cursor cursor=db.query(PetEntry.TABLE_NAME,   //   BAD PRACTISE--ACCESSING DATABASE DIRECTLY
                                      projection,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null);*/

     //INSTEAD USE A CONTENT_PROVIDER VIA A CONTENT_RESOLVER!
        cursor=getContentResolver().query(PetContract.CONTENT_URI,projection,null,null,null);


        int idColumnindex=cursor.getColumnIndex(PetEntry.COLUMN_ID);
        int nameColumnindex=cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        int breedColumnindex=cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
        int genderColumnindex=cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
        int weightColumnindex=cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
            displayView.append("\n"+cursor.getColumnName(0)+" - "+cursor.getColumnName(1)+" - "+cursor.getColumnName(2)+" - "
                    +cursor.getColumnName(3)+" - "+cursor.getColumnName(4)+" - ");
            while (cursor.moveToNext())
            {
                int id=cursor.getInt(idColumnindex);
                String name=cursor.getString(nameColumnindex);
                String breed=cursor.getString(breedColumnindex);
                int gender=cursor.getInt(genderColumnindex);
                int weight=cursor.getInt(weightColumnindex);

                displayView.append("\n"+id+" - "+name+" - "+breed+" - "+gender+" - "+weight);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
    private void insertPet()
    {
        ContentValues values=new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER,PetEntry.MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT,14);

        // Following code directly accesses database which is a bad practise, hence it is commented and replaced with
        // CONTENT_RESOLVER code (which in-turn calls provider).

        /*
            SQLiteDatabase db=mDbHelper.getWritableDatabase();
            long x= db.insert(PetEntry.TABLE_NAME,null,values);
        */

        Uri insertUri=getContentResolver().insert(PetContract.CONTENT_URI,values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.android.pets.data.PetContract.PetEntry;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kushal on 01-01-2018.
 */

/*
            -----------------~~~~~~~~~~CONTENT_PROVIDER AND CONTENT_RESOLVER NOTES~~~~~~~~~~~~~-----------------

-->PetProvider(we have made) is a "subclass" of the ContentProvider(it being an "abstract" class).Thus , we have to implement 5
    methods compulsorily->create,insert,update,delete and query().

-->The ContentProvider provides a very "robust abstraction layer" for our app's data.It is used to share data with other apps,load the data
    in UI of the same app itself,access other apps' data etc. Most importantly it keeps our data secured.(special permissions are required
    in manifest to access other apps data from ContentProvider so that malicious apps do not corrupt our database or tables).

-->A number of steps are involved in accessing the data of the same app from C.P. Firstly , we need a "ContentResolver object"
    in the activity which expects some info from the database. The  ContentResolver "implements identical methods (query,insert,update etc)".

--> ContentResolver "resolves" which provider it has to goto(for the requested data) with the help of
    "Uri" that is passed with it(for all the CRUD methods ,Uri is a parameter to be passed).

-->It asks for some info as a "client"(CR acts as a client) from the CP.The CP wraps the info in a "cursor object"
   and sends it back to the "UI that requested it."


*/

public class PetProvider extends ContentProvider {
    /*----------------------------URI_MATCHER NOTES---------------------------------

     -->There can be various types of data requests from an existing database(For example some query might request info of the full table,
         whereas some might just request a row,another might want to update or delete something).All these requests amount to different
         types of operations on the database.
         The ContentProvider must have some means to sort these requests .
     --> Each such varied request/query is called as a "uri pattern".
     --> We need a way to map all these patterns to corresponding operations that each request demands!
     -->"URI_MATCHER" does this for us .It maps each pattern to a unique integer constant (match),and for each such constant we can define
        the corresponding operation.

        */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    private static final int PETS=100;  // CONSTANTS TO WHICH A URI PATTERN WILL BE MAPPED
    private static final int PETS_ID=101;

    private static final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH); //CREATES A URI_MATCHER OBJECT

    static {

        matcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS); //MAPPING IS DONE HERE

        matcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS+"/#",PETS_ID);

    }
    //database helper object
   private PetdbHelper helper;

    @Override
    public boolean onCreate() {
        helper=new PetdbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        SQLiteDatabase database=helper.getReadableDatabase();
        Cursor cursor;
        projection=new String[]{PetEntry.COLUMN_ID,PetEntry.COLUMN_PET_NAME,PetEntry.COLUMN_PET_BREED,PetEntry.COLUMN_PET_GENDER
                ,PetEntry.COLUMN_PET_WEIGHT};

        int match=matcher.match(uri);
        switch (match)
        {
            case PETS:
                 cursor=database.query(PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PETS_ID:
                selection=PetEntry.COLUMN_ID+" =?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI "+uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    private Uri insertPet(Uri uri,ContentValues values)
    {
        SQLiteDatabase database=helper.getWritableDatabase();

        long id= database.insert(PetEntry.TABLE_NAME,null,values);
        if(id==-1)
        {
            Log.e(LOG_TAG,"FAILED TO INSERT ROW FOR "+uri);
            return null;
        }
        Uri newUri=ContentUris.withAppendedId(PetContract.BASE_CONTENT_URI,id);
        return newUri;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match=matcher.match(uri);

        switch(match)
        {
            case PETS:
                return insertPet(uri,values);
            default:
                throw new IllegalArgumentException("Insertion not supported with uri "+uri);

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}

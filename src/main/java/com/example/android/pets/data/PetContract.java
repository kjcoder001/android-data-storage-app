package com.example.android.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kushal on 25-12-2017.
 */

public final class PetContract {
    /*
               --------------~URI NOTES~--------------:-
        Uri stands for Uniform Resource Identifier.
        It has 3 parts .
        1->Scheme:- "content://"--every uri starts with this word
        2->Authority:-" its like an identity for the ContentProvider,name is same as that specified in the manifest of the app
         under attributes authorities.
        3->Type of Data:-usually a table_name in the database where info is there.

        --> Uri is passed as a parameter in every CRUD method of ContentResolver. The ContentResolver resolves(helps in identifying
         the correct ContentProvider) the provider to which  it has to establish a connection by focusing on the
         "authority" part of the Uri and subsequently accesses the data by  using the other parts of uri.

    */

    public static final String CONTENT_AUTHORITY="com.example.android.pets";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_PETS="pets";
    public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);



    private PetContract()
    {}
    public static  class PetEntry implements BaseColumns {

        public static final String TABLE_NAME="pets";

        public static final String COLUMN_ID=BaseColumns._ID;
        public static final String COLUMN_PET_NAME="name";
        public static final String COLUMN_PET_BREED="breed";
        public static final String COLUMN_PET_GENDER="gender";
        public static final String COLUMN_PET_WEIGHT="weight";

        public static final int UNKNOWN=0;
        public static final int MALE=1;
        public static final int FEMALE=2;


    }
}

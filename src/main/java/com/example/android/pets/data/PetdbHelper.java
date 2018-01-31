package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by kushal on 26-12-2017.
 */

public class PetdbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="shelter.db";
    public static final int DATABASE_VERSION=1;

    public static final String CREATE_QUERY="CREATE TABLE "+ PetEntry.TABLE_NAME+" ("+
            PetEntry.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            PetEntry.COLUMN_PET_NAME+" TEXT NOT NULL,"+
            PetEntry.COLUMN_PET_BREED+" TEXT,"+
            PetEntry.COLUMN_PET_GENDER+" INTEGER NOT NULL,"+
            PetEntry.COLUMN_PET_WEIGHT+" INTEGER NOT NULL DEFAULT 0);";

    public static final String DELETE_QUERY="DROP TABLE IF EXISTS "+PetEntry.TABLE_NAME+" ;";



    public PetdbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_QUERY);
        onCreate(db);

    }
}

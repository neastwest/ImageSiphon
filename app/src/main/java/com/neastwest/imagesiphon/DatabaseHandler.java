package com.neastwest.imagesiphon;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

class DatabaseHandler extends SQLiteOpenHelper {

    // All Static Variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "downloadedImages";

    // Downed Table name
    private static final String TABLE_DOWNED = "downed";

    // Downed Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_THUMB = "thumbnail";
    private static final String KEY_FULL = "full_size";

    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DOWNED + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_URL + " TEXT,"
                + KEY_THUMB + " TEXT," + KEY_FULL + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNED);

        // Create tabels again
        onCreate(db);
    }

    /**
     * All CRUD(create, read, update, delete) Operations
     */

    // Adding new contact
    void addDowned(Downed downed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_URL, downed.getURL()); // Downed URL
        values.put(KEY_THUMB, downed.getThumbnail()); // Downed Thumbnail

        // Insert Row
        db.insert(TABLE_DOWNED, null, values);
        db.close(); // close database connection
    }

    // Get Single Downed
    Downed getDowned(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DOWNED, new String[] { KEY_ID,
                KEY_URL, KEY_THUMB }, KEY_ID + "=?", new String[]
                { String.valueOf(id) }, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        Downed downed = new Downed(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        cursor.close();
        return downed;
    }

    // Get All Downed
    List<Downed> getAllDowned() {
        List<Downed> downedList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DOWNED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                Downed downed = new Downed(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2));
                // Add downed to list
                downedList.add(downed);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return downed list
        return downedList;
    }

    void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOWNED, null, null);
    }

    // Getting contacts Count
    int getDownedCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DOWNED;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}

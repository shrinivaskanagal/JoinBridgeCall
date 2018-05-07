package com.jbc.shrinivas.joinbridgecall.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.telecom.Call;

import java.util.ArrayList;
import java.util.List;

import models.CallDetail;

/**
 * Created by shrinivas on 4/6/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "callDetails";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_BRIDGE_NUMBER = "bridgeNumber";
    public static final String COLUMN_PASSCODE = "passcode";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PHONE_NUMBER + " TEXT,"
                    + COLUMN_BRIDGE_NUMBER + " TEXT,"
                    + COLUMN_PASSCODE + " TEXT"
                    + ")";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "callDetails_db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


    public long insertCallDetail(CallDetail callDetail) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_NAME, callDetail.getName());
        values.put(COLUMN_PHONE_NUMBER, callDetail.getPhoneNumber());
        values.put(COLUMN_BRIDGE_NUMBER, callDetail.getBridgeNumber());
        values.put(COLUMN_PASSCODE, callDetail.getPasscode());


        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public CallDetail getCallDetail(int id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PHONE_NUMBER, COLUMN_BRIDGE_NUMBER, COLUMN_PASSCODE},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        CallDetail callDetail = new CallDetail(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BRIDGE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PASSCODE)));

        // close the db connection
        cursor.close();

        return callDetail;
    }

    public List<CallDetail> getAllCallDetails() {
        List<CallDetail> callDetails = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CallDetail callDetail = new CallDetail();
                callDetail.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                callDetail.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                callDetail.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
                callDetail.setBridgeNumber(cursor.getString(cursor.getColumnIndex(COLUMN_BRIDGE_NUMBER)));
                callDetail.setPasscode(cursor.getString(cursor.getColumnIndex(COLUMN_PASSCODE)));
                callDetails.add(callDetail);

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return callDetails;
    }


    public int updateCallDetail(CallDetail callDetail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, callDetail.getName());
        values.put(COLUMN_PHONE_NUMBER, callDetail.getPhoneNumber());
        values.put(COLUMN_BRIDGE_NUMBER, callDetail.getBridgeNumber());
        values.put(COLUMN_PASSCODE, callDetail.getPasscode());
        // updating row
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(callDetail.getId())});
    }

    public void deleteCallDetail(CallDetail callDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(callDetail.getId())});
        db.close();
    }
}

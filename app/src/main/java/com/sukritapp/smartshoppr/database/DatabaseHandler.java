
package com.sukritapp.smartshoppr.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sukritapp.smartshoppr.util.SmartShopprApp;

import java.util.ArrayList;
import java.util.List;

/**
 * The DatabaseHandler class is responsible for all database related task.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "maddb";
    private static final String TABLE_ADDRESS = "palces";
    private static final String TABLE_IP = "IPAddress";
    private static final String KEY_ID = "id";
    private static final String KEY_IP = "ip";
    private static final String KEY_ADDRESS = "address";

    private static DatabaseHandler sDatabaseHandler = null;

    private DatabaseHandler() {
        super(SmartShopprApp.getApplication(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance() {
        if (null == sDatabaseHandler) {
            sDatabaseHandler = new DatabaseHandler();
        }
        return sDatabaseHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ADDRESS + " TEXT)";
        
        String CREATE_IPADDRESS_TABLE = "CREATE TABLE " + TABLE_IP + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IP + " TEXT)";
        db.execSQL(CREATE_IPADDRESS_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IP);

        onCreate(db);
    }

    public void insertIPAddress(String ipAddress) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IP, ipAddress);

        db.insert(TABLE_IP, null, values);
        db.close();
    }
    public void insertDestination(String place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS, place);

        db.insert(TABLE_ADDRESS, null, values);
        db.close(); 
    }
    public List<String> getAllIPAddress() {
        List<String> ipaddress = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_IP;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ipaddress.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ipaddress;
    }
    public List<String> getAllPlaces() {
        List<String> place = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                place.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return place;
    }
}

package com.example.ibook;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_ibook";
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "user_username";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String TABLE_CASHFLOW = "cashflow";
    private static final String COLUMN_CASHFLOW_ID = "cashflow_id";
    private static final String COLUMN_USERNAME_FK = "username";
    private static final String COLUMN_CASHFLOW_JENIS = "jenis";
    private static final String COLUMN_CASHFLOW_TGL = "tgl";
    private static final String COLUMN_CASHFLOW_NOMINAL = "nominal";
    private static final String COLUMN_CASHFLOW_KET = "keterangan";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT)";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private String CREATE_CASHFLOW_TABLE = "CREATE TABLE " + TABLE_CASHFLOW + "("
            + COLUMN_CASHFLOW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME_FK + " TEXT,"
            + COLUMN_CASHFLOW_JENIS + " TEXT," + COLUMN_CASHFLOW_TGL + " TEXT," +COLUMN_CASHFLOW_NOMINAL +
            " TEXT," +COLUMN_CASHFLOW_KET + " TEXT)";

    private String DROP_CASHFLOW_TABLE = "DROP TABLE IF EXISTS " + TABLE_CASHFLOW;

    /**
     * Constructor
     *
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CASHFLOW_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
        db.execSQL(DROP_CASHFLOW_TABLE);
        onCreate(db);
    }
    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void addCashflow(Cashflow cashflow, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME_FK, username);
        values.put(COLUMN_CASHFLOW_JENIS, cashflow.getJenis());
        values.put(COLUMN_CASHFLOW_TGL, cashflow.getTgl());
        values.put(COLUMN_CASHFLOW_NOMINAL, cashflow.getNominal());
        values.put(COLUMN_CASHFLOW_KET, cashflow.getKeterangan());
        db.insert(TABLE_CASHFLOW, null, values);
        db.close();
    }
    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    @SuppressLint("Range")
    public List<User> getAllUser() {
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USERNAME,
                COLUMN_USER_PASSWORD
        };
        String sortOrder =
                COLUMN_USERNAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.update(TABLE_USER, values, COLUMN_USERNAME + " = ?",
                new String[]{String.valueOf(user.getUsername())});
        db.close();
    }
    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    /**
     * This method to check user exist or not
     *
     * @param username
     * @return true/false
     */
    public boolean checkUser(String username) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    /**
     * This method to check user exist or not
     *
     * @param username
     * @param password
     * @return true/false
     */
    public boolean checkUser(String username, String password) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
         /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    @SuppressLint("Range")
    public List<Cashflow> getAllCashflow(String username) {
        String[] columns = {
                COLUMN_CASHFLOW_ID,
                COLUMN_CASHFLOW_JENIS,
                COLUMN_CASHFLOW_NOMINAL,
                COLUMN_CASHFLOW_KET,
                COLUMN_CASHFLOW_TGL
        };
        String sortOrder =
                COLUMN_CASHFLOW_TGL + " DESC";
        List<Cashflow> cashflowList = new ArrayList<Cashflow>();
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USERNAME_FK + " = ?";
        // selection arguments
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_CASHFLOW, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cashflow cashflow = new Cashflow();
                cashflow.setId_cashflow(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CASHFLOW_ID))));
                cashflow.setJenis(cursor.getString(cursor.getColumnIndex(COLUMN_CASHFLOW_JENIS)));
                cashflow.setNominal(cursor.getString(cursor.getColumnIndex(COLUMN_CASHFLOW_NOMINAL)));
                cashflow.setKeterangan(cursor.getString(cursor.getColumnIndex(COLUMN_CASHFLOW_KET)));
                cashflow.setTgl(cursor.getString(cursor.getColumnIndex(COLUMN_CASHFLOW_TGL)));
                // Adding user record to list
                cashflowList.add(cashflow);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return cashflowList;
    }
}

package com.s22010170.heathtrakerapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    // database name and version
    public static final String DATABASE_NAME = "HeathTracker.db";
    public static final int DATABASE_VERSION = 1;
    //TODO: table names
    public static final String USER_TABLE = "user_table";
    public static final String MEDICATION_TABLE = "medication_table";
    public static final String NOTIFICATION_TABLE = "notification_table";
    // column names
    //TODO: for user table
    public static final String user_COL_2 = "username";
    public static final String user_COL_3 = "email";
    public static final String user_COL_4 = "password";
    public static final String user_COL_5 = "imgAvatar";
    public static final String user_COL_6 = "imgBackground";
    public static final String user_COL_7 = "isLoggedIn";

    //TODO: for medication table
    public static final String medication_COL_2 = "medicationName";
    public static final String medication_COL_3 = "description";
    public static final String medication_COL_4 = "dosage";
    public static final String medication_COL_5 = "medicationImage";
    public static final String medication_COL_6 = "notificationTime";
    public static final String medication_COL_7 = "notificationRepeatTime";

    //TODO: for notification table
    public static final String notification_COL_2 = "resentMedicationName";
    public static final String notification_COL_3 = "resentDosage";
    public static final String notification_COL_4 = "resentNotificationTime";
    public static final String notification_COL_5 = "resentNotificationRepeatTime";



    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create user table
        db.execSQL("CREATE TABLE " + USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, EMAIL TEXT, PASSWORD TEXT, IMGAVATAR BLOB, IMGBACKGROUND BLOB, ISLOGGEDIN TEXT DEFAULT 'false')");
        // create medication table
        db.execSQL("CREATE TABLE " + MEDICATION_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, MEDICATIONNAME TEXT, DESCRIPTION TEXT, DOSAGE TEXT, MEDICATIONIMAGE BLOB, NOTIFICATIONTIME TEXT, NOTIFICATIONREPEATTIME TEXT)");
        // create notification table
        db.execSQL("CREATE TABLE " + NOTIFICATION_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, RESENTMEDICATIONNAME TEXT, RESENTDOSAGE TEXT, RESENTNOTIFICATIONTIME TEXT, RESENTNOTIFICATIONREPEATTIME TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEDICATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE);
        // create table
        onCreate(db);

    }

    //TODO: CRUD operations for User -----------------------------------------------------------------------------------

    // sign up method
    public boolean signUp(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_COL_2, username);
        contentValues.put(user_COL_3, email);
        contentValues.put(user_COL_4, password);
        contentValues.put(user_COL_7, "true");

        long result = db.insert(USER_TABLE, null, contentValues);

        return result != -1;
    }
    // sign in method
    public Cursor signIn(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE EMAIL = '" + email + "' AND PASSWORD = '" + password + "'" , null);
    }
    // update user login status
    public boolean updateLoginStatus(String email, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_COL_7, status);

        long result = db.update(USER_TABLE, contentValues, "EMAIL = ?", new String[]{email});

        return result != -1;
    }
    // reset password method
    public boolean resetPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_COL_4, password);

        long result = db.update(USER_TABLE, contentValues, "EMAIL = ?", new String[]{email});

        return result != -1;
    }
    // delete account method
    public boolean deleteAccount(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(USER_TABLE, "EMAIL = ?", new String[]{email});

        return result != -1;
    }
    // get user data
    public Cursor getUserData(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE EMAIL = '" + email + "'", null);
    }
    // update user data
    public boolean updateUserData(String oldEmail, String email, String username, String password, byte[] imgAvatar, byte[] imgBackground) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_COL_2, username);
        contentValues.put(user_COL_3, email);
        contentValues.put(user_COL_4, password);
        contentValues.put(user_COL_5, imgAvatar);
        contentValues.put(user_COL_6, imgBackground);
        contentValues.put(user_COL_7, "true");

        long result = db.update(USER_TABLE, contentValues, "EMAIL = ?", new String[]{oldEmail});

        return result != -1;
    }

    //TODO: CRUD operations for Medication -----------------------------------------------------------------------------------

    // insert medication
    public boolean insertMedication(String medicationName, String description, String dosage, byte[] medicationImage, String notificationTime, String notificationRepeatTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(medication_COL_2, medicationName);
        contentValues.put(medication_COL_3, description);
        contentValues.put(medication_COL_4, dosage);
        contentValues.put(medication_COL_5, medicationImage);
        contentValues.put(medication_COL_6, notificationTime);
        contentValues.put(medication_COL_7, notificationRepeatTime);

        long result = db.insert(MEDICATION_TABLE, null, contentValues);

        return result != -1;
    }

    // get all medications
    public Cursor getAllMedications() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + MEDICATION_TABLE, null);
    }

    // get medication by id
    public Cursor getMedicationById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + MEDICATION_TABLE + " WHERE ID = " + id, null);
    }

    // update medication
    public boolean updateMedication(int id, String medicationName, String description, String dosage, byte[] medicationImage, String notificationTime, String notificationRepeatTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(medication_COL_2, medicationName);
        contentValues.put(medication_COL_3, description);
        contentValues.put(medication_COL_4, dosage);
        contentValues.put(medication_COL_5, medicationImage);
        contentValues.put(medication_COL_6, notificationTime);
        contentValues.put(medication_COL_7, notificationRepeatTime);

        long result = db.update(MEDICATION_TABLE, contentValues, "ID = ?", new String[]{String.valueOf(id)});

        return result != -1;
    }

    // delete one medication
    public boolean deleteMedication(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(MEDICATION_TABLE, "ID = ?", new String[]{String.valueOf(id)});

        return result != -1;
    }

    // delete all medications
    public boolean deleteAllMedications() {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(MEDICATION_TABLE, null, null);

        return result != -1;
    }

    //TODO: CRUD operations for Notification -----------------------------------------------------------------------------------

    // insert notification
    public boolean insertNotification(String medicationName, String dosage, String notificationTime, String notificationRepeatTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(notification_COL_2, medicationName);
        contentValues.put(notification_COL_3, dosage);
        contentValues.put(notification_COL_4, notificationTime);
        contentValues.put(notification_COL_5, notificationRepeatTime);

        long result = db.insert(NOTIFICATION_TABLE, null, contentValues);

        return result != -1;
    }

    // get medications form notification table
    // eg: get all medications that have notification time less than current time
    public Cursor getAllResentNotifications(String currentTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + NOTIFICATION_TABLE + " WHERE RESENTNOTIFICATIONTIME < '" + currentTime + "'", null);
    }

    public Cursor getAllNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + NOTIFICATION_TABLE, null);
    }

    // update notification
    public boolean updateNotification(int id, String medicationName, String dosage, String notificationTime, String notificationRepeatTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(notification_COL_2, medicationName);
        contentValues.put(notification_COL_3, dosage);
        contentValues.put(notification_COL_4, notificationTime);
        contentValues.put(notification_COL_5, notificationRepeatTime);

        long result = db.update(NOTIFICATION_TABLE, contentValues, "ID = ?", new String[]{String.valueOf(id)});

        return result != -1;
    }

    // delete one notification
    public boolean deleteNotification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(NOTIFICATION_TABLE, "ID = ?", new String[]{String.valueOf(id)});

        return result != -1;
    }

    // delete all notifications
    public boolean deleteAllNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(NOTIFICATION_TABLE, null, null);

        return result != -1;
    }
}

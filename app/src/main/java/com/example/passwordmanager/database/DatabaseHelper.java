package com.example.passwordmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.passwordmanager.models.Password;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PasswordManager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String TABLE_PASSWORDS = "passwords";
    private static final String COLUMN_PASSWORD_ID = "password_id";
    private static final String COLUMN_MASTER_USERNAME = "master_username";
    private static final String COLUMN_SERVICE_NAME = "service_name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NOTES = "notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы пользователей
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_USERNAME + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Создание таблицы паролей
        String CREATE_PASSWORDS_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + "("
                + COLUMN_PASSWORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MASTER_USERNAME + " TEXT,"
                + COLUMN_SERVICE_NAME + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_NOTES + " TEXT" + ")";
        db.execSQL(CREATE_PASSWORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
        onCreate(db);
    }

    // Методы для работы с пользователями
    public long addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_USERNAME + " = ?",
                new String[]{username},
                null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_USERNAME + " = ? AND " + COLUMN_USER_PASSWORD + " = ?",
                new String[]{username, password},
                null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // Методы для работы с паролями
    public long addPassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MASTER_USERNAME, password.getMasterUsername());
        values.put(COLUMN_SERVICE_NAME, password.getServiceName());
        values.put(COLUMN_USERNAME, password.getUsername());
        values.put(COLUMN_PASSWORD, password.getPassword());
        values.put(COLUMN_NOTES, password.getNotes());
        long result = db.insert(TABLE_PASSWORDS, null, values);
        db.close();
        return result;
    }

    public List<Password> getAllPasswords(String masterUsername) {
        List<Password> passwordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PASSWORDS,
                new String[]{COLUMN_PASSWORD_ID, COLUMN_SERVICE_NAME, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_NOTES},
                COLUMN_MASTER_USERNAME + " = ?",
                new String[]{masterUsername},
                null, null, COLUMN_SERVICE_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Password password = new Password(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_ID)),
                        masterUsername,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES))
                );
                passwordList.add(password);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return passwordList;
    }

    public Password getPassword(int passwordId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Password password = null;
        Cursor cursor = db.query(TABLE_PASSWORDS,
                new String[]{COLUMN_PASSWORD_ID, COLUMN_MASTER_USERNAME,
                        COLUMN_SERVICE_NAME, COLUMN_USERNAME,
                        COLUMN_PASSWORD, COLUMN_NOTES},
                COLUMN_PASSWORD_ID + " = ?",
                new String[]{String.valueOf(passwordId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            password = new Password(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MASTER_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES))
            );
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return password;
    }

    public int deletePassword(int passwordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PASSWORDS,
                COLUMN_PASSWORD_ID + " = ?",
                new String[]{String.valueOf(passwordId)});
        db.close();
        return result;
    }

    public int updatePassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, password.getServiceName());
        values.put(COLUMN_USERNAME, password.getUsername());
        values.put(COLUMN_PASSWORD, password.getPassword());
        values.put(COLUMN_NOTES, password.getNotes());

        int result = db.update(TABLE_PASSWORDS,
                values,
                COLUMN_PASSWORD_ID + " = ?",
                new String[]{String.valueOf(password.getId())});
        db.close();
        return result;
    }
}
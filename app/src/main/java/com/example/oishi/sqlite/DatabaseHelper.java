package com.example.oishi.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //데이터베이스
    public static final String DATABASE = "Oishi_User.db";
    public static final int DATABASE_VERSION = 1;

    //테이블
    public static final String FAVORITE_TABLE = "User_Favorite";
    public static final String PAYMENT_TABLE = "User_Payment";
    public static final String ORDER_TABLE = "User_Order";

    //속성
    public static final String USER_ID = "_id";
    public static final String USER_FAVORITE_STORE = "favorite_store";
    public static final String USER_IMMEDIATE_PAYMENT = "immediate_payment";
    public static final String USER_PHONE_ORDER = "phone_order";

    public static final String[] ALL_FAVORITE_COLUMNS = {USER_ID, USER_FAVORITE_STORE};
    public static final String[] ALL_PAYMENT_COLUMNS = {USER_ID, USER_IMMEDIATE_PAYMENT};
    public static final String[] ALL_ORDER_COLUMNS = {USER_ID, USER_PHONE_ORDER};

    //SQL(CREATE TABLE)
    private static final String CREATE_FAVORITE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + FAVORITE_TABLE + "( "
                    + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USER_FAVORITE_STORE + " TEXT)";

    private static final String CREATE_PAYMENT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PAYMENT_TABLE + "( "
                    + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USER_IMMEDIATE_PAYMENT + " TEXT)";

    private static final String CREATE_ORDER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ORDER_TABLE + "( "
                    + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USER_PHONE_ORDER + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_PAYMENT_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAYMENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        onCreate(db);
    }
}

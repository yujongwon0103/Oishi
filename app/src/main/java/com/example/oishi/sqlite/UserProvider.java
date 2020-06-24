package com.example.oishi.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;

public class UserProvider extends ContentProvider {
    //패키지명
    private static final String AUTHORITY = "com.example.oishi";
    //테이블 명
    private static final String PATH1 = "User_Favorite";
    private static final String PATH2 = "User_Payment";
    private static final String PATH3 = "User_Order";
    //각 테이블 별 URI
    public static final Uri CONTENT_URI1 = Uri.parse("content://" + AUTHORITY + "/" + PATH1);
    public static final Uri CONTENT_URI2 = Uri.parse("content://" + AUTHORITY + "/" + PATH2);
    public static final Uri CONTENT_URI3 = Uri.parse("content://" + AUTHORITY + "/" + PATH3);

    private static final int Favorites = 1;
    private static final int Favorite_ID = 2;

    private static final int Payments = 3;
    private static final int Payment_ID = 4;

    private static final int Orders = 5;
    private static final int Order_ID = 6;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, PATH1, Favorites);
        uriMatcher.addURI(AUTHORITY, PATH1 + "/#", Favorite_ID);

        uriMatcher.addURI(AUTHORITY, PATH2, Payments);
        uriMatcher.addURI(AUTHORITY, PATH2 + "/#", Payment_ID);

        uriMatcher.addURI(AUTHORITY, PATH3, Orders);
        uriMatcher.addURI(AUTHORITY, PATH3 + "/#", Order_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        database = helper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case Favorites:
                cursor = database.query(DatabaseHelper.FAVORITE_TABLE,
                        DatabaseHelper.ALL_FAVORITE_COLUMNS,
                        selection, null, null, null, DatabaseHelper.USER_FAVORITE_STORE + " ASC");
                break;
            case Payments:
                cursor = database.query(DatabaseHelper.PAYMENT_TABLE,
                        DatabaseHelper.ALL_PAYMENT_COLUMNS,
                        selection, null, null, null, DatabaseHelper.USER_IMMEDIATE_PAYMENT + " ASC");
                break;
            case Orders:
                cursor = database.query(DatabaseHelper.ORDER_TABLE,
                        DatabaseHelper.ALL_ORDER_COLUMNS,
                        selection, null, null, null, DatabaseHelper.USER_PHONE_ORDER + " ASC");
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Favorites:
                return "vnd.android.cursor.dir/favorites";
            case Payments:
                return "vnd.android.cursor.dir/payments";
            case Orders:
                return "vnd.android.cursor.dir/orders";
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id;
        Uri _uri;
        if (uriMatcher.match(uri) == Favorites) {
            id = database.insert(DatabaseHelper.FAVORITE_TABLE, null, values);

            if(id > 0) {
                _uri = ContentUris.withAppendedId(CONTENT_URI1, id);
                if(getContext() != null) {
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                return _uri;
            }
        }
        else if (uriMatcher.match(uri) == Payments) {
            id = database.insert(DatabaseHelper.PAYMENT_TABLE, null, values);

            if(id > 0) {
                _uri = ContentUris.withAppendedId(CONTENT_URI2, id);
                if(getContext() != null) {
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                return _uri;
            }
        }
        else if (uriMatcher.match(uri) == Orders) {
            id = database.insert(DatabaseHelper.ORDER_TABLE, null, values);

            if(id > 0) {
                _uri = ContentUris.withAppendedId(CONTENT_URI3, id);
                if(getContext() != null) {
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                return _uri;
            }
        }

        throw new SQLException("추가 실패-> URI : " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;

        if (uriMatcher.match(uri) == Favorites) {
            count = database.delete(DatabaseHelper.FAVORITE_TABLE, selection, selectionArgs);
        }
        else if (uriMatcher.match(uri) == Payments) {
            count = database.delete(DatabaseHelper.PAYMENT_TABLE, selection, selectionArgs);
        }
        else if (uriMatcher.match(uri) == Orders) {
            count = database.delete(DatabaseHelper.ORDER_TABLE, selection, selectionArgs);
        }
        else {
            throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;

        if (uriMatcher.match(uri) == Favorites) {
            count = database.update(DatabaseHelper.FAVORITE_TABLE, values, selection, selectionArgs);
        }
        else if (uriMatcher.match(uri) == Payments) {
            count = database.update(DatabaseHelper.PAYMENT_TABLE, values, selection, selectionArgs);
        }
        else if (uriMatcher.match(uri) == Orders) {
            count = database.update(DatabaseHelper.ORDER_TABLE, values, selection, selectionArgs);
        }
        else {
            throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}

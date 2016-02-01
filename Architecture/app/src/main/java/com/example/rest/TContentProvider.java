package com.example.rest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

public class TContentProvider extends ContentProvider{
    private static final String TAG = "TContentProvider";
    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "test";
    public static final String AUTHORITY = "com.example.rest.providers.TContentProvider";
    private static final UriMatcher sUriMatcher;
    private static final int TEST = 1;
    private static final int TEST_ID = 2;
    private static HashMap<String, String> testProjectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, TEST);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", TEST_ID);

        testProjectionMap = new HashMap<>();
        testProjectionMap.put(Tests.Test.TEST_ID, Tests.Test.TEST_ID);
        testProjectionMap.put(Tests.Test.TITLE, Tests.Test.TITLE);
        testProjectionMap.put(Tests.Test.TEXT, Tests.Test.TEXT);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("DatabaseHelper", "Create new database");
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + Tests.Test.TEST_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Tests.Test.TITLE + " VARCHAR(255)," + Tests.Test.TEXT + " LONGTEXT" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        Log.d("TContentProvider", "onCreate() on content provider!");
        dbHelper = new DatabaseHelper(getContext());
        return dbHelper != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        qb.setProjectionMap(testProjectionMap);

        switch (sUriMatcher.match(uri)) {
            case TEST:
                break;
            case TEST_ID:
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case TEST:
                return Tests.Test.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;
        switch (sUriMatcher.match(uri)) {
            case TEST:
                id = db.insert(TABLE_NAME, null, values);
                break;
            default:
                throw new UnsupportedOperationException("OPS");

        }
        if (id > -1) {
            uri = uri.buildUpon().appendPath(String.valueOf(id)).build();
            getContext().getContentResolver().notifyChange(uri, null);
        } else
            uri = null;
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}

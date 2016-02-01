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
import static com.example.config.YouGidContract.*;

public class YouGidContentProvider extends ContentProvider{
    private static UriMatcher mUriMatcher;
    private DBHelper mDBHelper;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, Marks.TABLE_NAME, Marks.DIR);
        mUriMatcher.addURI(AUTHORITY, Marks.TABLE_NAME+"/#", Marks.ITEM);
    }

    static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder sb = new StringBuilder().append("create table ").append(Marks.TABLE_NAME).append("(")
                    .append(Marks._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                    .append(Marks.SNIPPET).append("  TEXT,")
                    .append(Marks.TEXT).append(" TEXT,")
                    .append(Marks.LATITUDE).append(" REAL,")
                    .append(Marks.LONGITUDE).append(" REAL,")
                    .append(Marks.OWNER).append(" INTEGER").append(")");
            db.execSQL(sb.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Marks.TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return mDBHelper != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Marks.TABLE_NAME);
        boolean remoteSync = false;

        switch (mUriMatcher.match(uri)) {
            case Marks.DIR:
                break;
            case Marks.ITEM:
                selection += Marks._ID + "=" + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("NO PATH case found");
        }
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor c = db.query(Marks.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case Marks.DIR:
                return Marks.DIR_TYPE;
            case Marks.ITEM:
                return Marks.ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long id;
        switch (mUriMatcher.match(uri)) {
            case Marks.DIR:
                id = db.insert(Marks.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("From insert: Path is not correct");
        }
        if (id > -1) {
            uri = uri.buildUpon().appendPath(String.valueOf(id)).build();
            getContext().getContentResolver().notifyChange(uri, null);
            return uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case Marks.DIR:
                break;
            default:
                throw new IllegalArgumentException("From insert: Path is not correct");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return db.delete(Marks.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case Marks.DIR:
                break;
            default:
                throw new IllegalArgumentException("From insert: Path is not correct");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return db.update(Marks.TABLE_NAME, values, selection, selectionArgs);
    }
}

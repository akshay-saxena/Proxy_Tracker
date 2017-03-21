package com.project.saxena.akshay.attendance_management.model;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.project.saxena.akshay.attendance_management.AppConstants;

import static com.project.saxena.akshay.attendance_management.AppConstants.ContentProviderContentType.URI_STUDENTATTENDANCE;

/**
 * Created by Akshay on 3/20/2017.
 */

public class Content_Provider extends ContentProvider {
    private static UriMatcher mUriMatcher;
    private Database_helper mDatabasehelper;
    private SQLiteDatabase database;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AppConstants.DatabaseConstants.AUTHORITY,
                AppConstants.DATABASE_TABLENAMES.TABLE_STUDENT_RECORD, AppConstants.ContentProviderContentType.URI_STUDENTATTENDANCE);
        mUriMatcher.addURI(AppConstants.DatabaseConstants.AUTHORITY,
                AppConstants.DATABASE_TABLENAMES.TABLE_SUBJECTS, AppConstants.ContentProviderContentType.URI_STUDENT_SUBJECTS);

    }


    @Override
    public boolean onCreate() {
        mDatabasehelper = new Database_helper(getContext());
        database = mDatabasehelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase database = mDatabasehelper.getReadableDatabase();
        final String tableName = getTableName(mUriMatcher.match(uri));

        if (tableName != null) {
            cursor = database.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowId = -1;
        final String tableName = getTableName(mUriMatcher.match(uri));
        try {
            database.beginTransactionNonExclusive();

        } finally {
            database.endTransaction();
            rowId = database.insert(tableName, null, values);
            database.setTransactionSuccessful();
        }
        if (rowId != -11) {
            final Uri insertUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
            return insertUri;
        }

        return null;
    }

    private String getTableName(int contentType) {
        String table_name = "null";
        switch (contentType){
            case AppConstants.ContentProviderContentType.URI_STUDENTATTENDANCE:
                table_name =  AppConstants.DATABASE_TABLENAMES.TABLE_STUDENT_RECORD;
                break;
            case AppConstants.ContentProviderContentType.URI_STUDENT_SUBJECTS:
                table_name =  AppConstants.DATABASE_TABLENAMES.TABLE_SUBJECTS;
                break;
        }
        return table_name;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDatabasehelper.getWritableDatabase();
        final String tableName = getTableName(mUriMatcher.match(uri));

        try {
            database.beginTransaction();
            int count = database.delete(tableName, selection, selectionArgs);
            database.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        final String tableName = getTableName(mUriMatcher.match(uri));
        try {
            database.beginTransaction();
            count = database.update(tableName, values, selection, selectionArgs);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDatabasehelper.getWritableDatabase();
        final int contentType = mUriMatcher.match(uri);
        switch (contentType) {
                case AppConstants.ContentProviderContentType.URI_STUDENTATTENDANCE: {
                    db.beginTransaction();
                    int returnCount = 0;
                    try {
                        for (ContentValues value : values) {
                            long _id = db.insert(getTableName(mUriMatcher.match(uri)),
                                    null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return returnCount;
                }
                default:
                    return super.bulkInsert(uri, values);
            }
        }
}

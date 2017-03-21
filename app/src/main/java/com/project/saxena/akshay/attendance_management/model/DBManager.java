package com.project.saxena.akshay.attendance_management.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.project.saxena.akshay.attendance_management.AppConstants;
import com.project.saxena.akshay.attendance_management.ProxyApplication;

import java.util.List;

/**
 * Created by Akshay on 3/20/2017.
 */

public class DBManager {
    private Database_helper dbHelper;

    private SQLiteDatabase database;
    private Context mContext;
    private static DBManager mInstance;
    private static final String TAG = DBManager.class.getName();

    public DBManager(Context mContext) {
        this.mContext = mContext;
    }


    public DBManager open() throws SQLException {
        dbHelper = new Database_helper(mContext);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long bulkInsertStudentRecords(List<Student> studentList){

        ContentValues[] valuesList = null;
        if (studentList.size() > 0) {
             valuesList = new ContentValues[studentList.size()];
            for (int i = 0; i < studentList.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_NAME, studentList.get(i).getName());
                contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_BRANCH, studentList.get(i).getBranch());
                contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_YEAR, studentList.get(i).getPursuingYear());
                contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_ROLLNO, studentList.get(i).getRollNumber());
                contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.CURRENT_SUBJECT, ((ProxyApplication)mContext.getApplicationContext()).
                        getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_SUBJECT));
                contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.ATTENDACE_DATE, System.currentTimeMillis());
                valuesList[i] = contentValues;
            }
        }

        long row = mContext.getContentResolver().bulkInsert(AppConstants.CONTENT_URIS.STUDENT_TABLE_URI, valuesList);
        Log.d(TAG,"******row inserted******"+row);
        return row;
    }

    public void insertStudentRecords(Student object){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_NAME, object.getName());
        contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_BRANCH,object.getBranch());
        contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_YEAR,object.getPursuingYear());
        mContext.getContentResolver().insert(AppConstants.CONTENT_URIS.STUDENT_TABLE_URI, contentValues);
    }

    public Cursor readTable(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, String sortOrderBYColoumn) {
        if (sortOrderBYColoumn == null)
            return mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        else
            return mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrderBYColoumn + " " + sortOrder);
    }

   /* public int updateRecords(String URI, Student object) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_NAME, object.getName());
        contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_BRANCH,object.getBranch());
        contentValues.put(AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_YEAR,object.getPursuingYear());
        int row = database.update(AppConstants.DATABASE_TABLENAMES.TABLE_STUDENT_RECORD, contentValues,
                AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_ROLLNO + " =? " + object.getRollNumber(), null);
        return row;
    }*/


}


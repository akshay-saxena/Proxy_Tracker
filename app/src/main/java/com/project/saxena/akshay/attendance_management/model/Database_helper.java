package com.project.saxena.akshay.attendance_management.model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.project.saxena.akshay.attendance_management.AppConstants;

/**
 * Created by Akshay on 3/20/2017.
 */

public class Database_helper extends SQLiteOpenHelper {



    public Database_helper(Context context) {
        super(context, AppConstants.DatabaseConstants.DATABASE_NAME, null, AppConstants.DatabaseConstants.
        DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateStudentTableQuery());
        db.execSQL(getCreateSubjectsQuery());
        db.execSQL(insertDefaultSubjectsinToSubjectsTable());

    }

    public String getCreateStudentTableQuery() {
        String query = "null";
        try {
            query = "CREATE TABLE IF NOT EXISTS " + AppConstants.DATABASE_TABLENAMES.TABLE_STUDENT_RECORD+ " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_NAME + " TEXT NOT NULL, "
                    + AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_ROLLNO + " INTEGER NOT NULL, "
                    + AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_BRANCH + " TEXT NOT NULL, "
                    + AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.STUDENT_YEAR + " INTEGER NOT NULL, "
                    + AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.ATTENDACE_DATE + " INTEGER NOT NULL, "
                    + AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.CURRENT_SUBJECT + " TEXT NOT NULL, "
                    + AppConstants.STUDENT_RECORDS_TABLE_COLOUMS.ISSYNC + " INTEGER DEFAULT 0);  " ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }

    public String getCreateSubjectsQuery() {
        String query = "null";
        try {
            query = "CREATE TABLE IF NOT EXISTS " + AppConstants.DATABASE_TABLENAMES.TABLE_SUBJECTS+ " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + AppConstants.SUBJECTS_TABLE_COLOUMS.SUBJECTS_NAME + " TEXT NOT NULL);  " ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }

    public String insertDefaultSubjectsinToSubjectsTable() {
        String query = "null";
        try {
            query = "INSERT INTO "+AppConstants.DATABASE_TABLENAMES.TABLE_SUBJECTS+
                    " VALUES (0,"+"'COMPUTER NETWORKS'"+");" ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Database_helper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.DATABASE_TABLENAMES.TABLE_STUDENT_RECORD);
        onCreate(db);

    }


}

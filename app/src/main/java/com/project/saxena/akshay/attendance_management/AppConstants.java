package com.project.saxena.akshay.attendance_management;

import android.net.Uri;

/**
 * Created by nitin.saxena on 3/7/2017.
 */

public class AppConstants {
    public interface SharePreferencesConstants {
        String BRANCH_NAME = "branch";
        String YEAR = "year";
        String SEMESTER = "semester";
        String SELECTED_SUBJECT = "selected_subject";
        String SELECTED_TIME = "selected_time";
        String SAVED_DATE = "datesaved";
    }
    public interface DatabaseConstants{
        String AUTHORITY = "com.akshay.studentInformation";
        String DATABASE_NAME="StudentAttendanceRecord";
        int DATABASE_VERSION=1;
    }

    public interface DATABASE_TABLENAMES{
        String TABLE_STUDENT_RECORD="StudentInforamation";
        String TABLE_SUBJECTS = "subjects";
    }

    public interface STUDENT_RECORDS_TABLE_COLOUMS {
        String STUDENT_NAME="StudentName";
        String STUDENT_ROLLNO="StudentRollNo";
        String STUDENT_BRANCH="StudentBranch";
        String STUDENT_YEAR="StudentYear";
        String ATTENDACE_DATE="date";
        String CURRENT_SUBJECT = "subject";
        String ISSYNC="isdatasync";
    }

    public interface SUBJECTS_TABLE_COLOUMS {
        String SUBJECTS_NAME="subjectsName";
    }

    public interface CONTENT_URIS{
        public static final Uri STUDENT_TABLE_URI = Uri.parse("content://" + DatabaseConstants.AUTHORITY + "/" + DATABASE_TABLENAMES.TABLE_STUDENT_RECORD);
        public static final Uri SUBJECTS_TABLE_URI = Uri.parse("content://" + DatabaseConstants.AUTHORITY + "/" + DATABASE_TABLENAMES.TABLE_SUBJECTS);
    }
    public interface ContentProviderContentType{
        int URI_STUDENTATTENDANCE = 1;
        int URI_STUDENT_SUBJECTS = 2;
    }

    public interface Loaders_IDS{
        int SUBJECT_LOADER_ID = 1;
        int STUDENT_ATTENDANCE_LIST = 2;
    }
}

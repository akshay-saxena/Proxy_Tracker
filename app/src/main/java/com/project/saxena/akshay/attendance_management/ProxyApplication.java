package com.project.saxena.akshay.attendance_management;

import android.app.Application;
import android.content.Context;

import com.project.saxena.akshay.attendance_management.model.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Akshay on 3/16/2017.
 */

public class ProxyApplication extends Application {

    public HashSet<Student> scannedStudentList;

    public ProxyStudentPreference preferences;

    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        scannedStudentList = new HashSet<>();
        preferences =  new ProxyStudentPreference(this);
        context = this;
    }

    public Context getContext(){
        return context;
    }

    public HashSet<Student> getScannedStudentList() {
        return scannedStudentList;
    }

    public  void addScannedStudentList(Student studentObject) {
        scannedStudentList.add(studentObject);
    }

    public void deletewholeDatafromScannedList(){
        scannedStudentList.clear();
    }

    public ProxyStudentPreference getPreferences(){
        return preferences;
    }
}

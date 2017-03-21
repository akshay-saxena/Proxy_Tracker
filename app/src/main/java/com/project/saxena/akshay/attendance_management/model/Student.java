package com.project.saxena.akshay.attendance_management.model;

import android.util.Log;

/**
 * Created by nitin.saxena on 3/6/2017.
 */

public class Student {
    private String name;
    private String rollNumber;
    private String pursuingYear;
    private String branch;

    private String currentSubject;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPursuingYear() {
        return pursuingYear;
    }

    public void setPursuingYear(String pursuingYear) {
        this.pursuingYear = pursuingYear;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String semester;
    private String age;


    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCurrentSubject() {
        return currentSubject;
    }

    public void setCurrentSubject(String currentSubject) {
        this.currentSubject = currentSubject;
    }


    @Override
    public int hashCode() {
        Log.d("NITIN", "hascode::");
        return name.length();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Student)
        {
            Student c = (Student) o;
            if ( this.name.equals(c.getName()) ) //whatever here
                return true;
        }
        return false;
    }




}

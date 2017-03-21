package com.project.saxena.akshay.attendance_management;

import com.project.saxena.akshay.attendance_management.model.Student;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Akshay on 3/21/2017.
 */

public class ResponseParserManager {

    public Student parseResponse(String response) {
        Student studentObject = null;
        try {
            JSONObject object = new JSONObject(response);
            studentObject = null;
            if (object != null) {
                studentObject = new Student();
                studentObject.setName(object.getString("name"));
                studentObject.setRollNumber(object.getString("rollNo"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return studentObject;
    }
}

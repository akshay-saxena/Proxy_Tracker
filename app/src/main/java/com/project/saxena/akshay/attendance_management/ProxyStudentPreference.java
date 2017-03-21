package com.project.saxena.akshay.attendance_management;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nitin.saxena on 3/6/2017.
 */

public class ProxyStudentPreference {
    Set<String> set = new HashSet<>();
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Context mContext;

    public ProxyStudentPreference(Context context) {
        mContext = context;
        pref = ((ProxyApplication) context.getApplicationContext()).getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setStringAttributes(String keyName, String keyValue) {
        editor.putString(keyName, keyValue);
        editor.commit();
    }

    public String getStringAttributes(String keyName) {
        return pref.getString(keyName, null);
    }

    public void setLongAttributes(String keyName, long keyValue) {
        editor.putLong(keyName, keyValue);
        editor.commit();
    }

    public Long getLongAttributes(String keyName) {
        return pref.getLong(keyName, -1);
    }
}

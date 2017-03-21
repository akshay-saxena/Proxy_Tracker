package com.project.saxena.akshay.attendance_management.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.saxena.akshay.attendance_management.ProxyApplication;
import com.project.saxena.akshay.attendance_management.R;
import com.project.saxena.akshay.attendance_management.model.Student;


/**
 * Created by nitin.saxena on 3/7/2017.
 */

public class AddAttendantStudentFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    Spinner branchSelect, YearSelect, SemesterSelect;
    String year, branch, semester;
    AppCompatButton buttonSave;
    AppCompatEditText nameEditText, rollNumberText;

    // newInstance constructor for creating fragment with arguments
    public static AddAttendantStudentFragment newInstance(int page, String title) {
        AddAttendantStudentFragment fragmentFirst = new AddAttendantStudentFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.input_information_attendance, container, false);
        Log.d("NITIN", "*****InputAttendanceInformation***");
        branchSelect = (Spinner) rootView.findViewById(R.id.branch_select);
        YearSelect = (Spinner) rootView.findViewById(R.id.year_select);
        SemesterSelect = (Spinner) rootView.findViewById(R.id.semester_select);
        buttonSave = (AppCompatButton) rootView.findViewById(R.id.save_button);
        nameEditText = (AppCompatEditText) rootView.findViewById(R.id.name_edittext);
        rollNumberText = (AppCompatEditText) rootView.findViewById(R.id.rollnumber_edittext);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(branch) && !TextUtils.isEmpty(year)
                        && !TextUtils.isEmpty(semester)) {
                    Student studentObject = new Student();
                    if (TextUtils.isEmpty(nameEditText.getText().toString()))
                        studentObject.setName(nameEditText.getText().toString());
                    if (TextUtils.isEmpty(rollNumberText.getText().toString()))
                        studentObject.setRollNumber(rollNumberText.getText().toString());
                    ((ProxyApplication) getActivity().getApplicationContext()).addScannedStudentList(studentObject);
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter values in all the fields", Toast.LENGTH_SHORT).show();
            }
        });
        branchSelect.setOnItemSelectedListener(this);
        YearSelect.setOnItemSelectedListener(this);
        SemesterSelect.setOnItemSelectedListener(this);
        return rootView;
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.branch_select) {
            branch = parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.year_select) {
            year = parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.semester_select) {
            semester = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

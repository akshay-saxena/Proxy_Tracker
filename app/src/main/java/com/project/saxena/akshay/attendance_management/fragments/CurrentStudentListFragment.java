package com.project.saxena.akshay.attendance_management.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.saxena.akshay.attendance_management.AppConstants;
import com.project.saxena.akshay.attendance_management.ProxyApplication;
import com.project.saxena.akshay.attendance_management.R;
import com.project.saxena.akshay.attendance_management.UserAreaActivity;
import com.project.saxena.akshay.attendance_management.adapters.CustomStudentLIstAdapter;
import com.project.saxena.akshay.attendance_management.model.Student;

import java.util.ArrayList;


/**
 * Created by nitin.saxena on 3/2/2017.
 */

public class CurrentStudentListFragment extends TabFragment implements AdapterView.OnItemSelectedListener,LoaderManager.LoaderCallbacks<Cursor>,
                                                    View.OnClickListener
{

    RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton floatingActionButton;

    private ScrollView parentAddAttendantStudentLayout;
    private CoordinatorLayout parentListLayout;
    private Context context;

    Spinner branchSelect, YearSelect, SemesterSelect;
    String year, branch, semester;
    AppCompatButton buttonSave, cancelButton;
    AppCompatEditText nameEditText, rollNumberText;
    ArrayList<Student> studentPresentList;
    CurrentStudentListFragment fragmentFirstInstance;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context= context;

    }

    // newInstance constructor for creating fragment with arguments
    public static CurrentStudentListFragment newInstance(int page, String title) {
        CurrentStudentListFragment fragmentFirst = new CurrentStudentListFragment();
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
        final View rootView = inflater.inflate(R.layout.studentscan_list, container, false);
        Log.d("NITIN", "*****CurrentStudentListFragment***");


        getLoaderManager().restartLoader(AppConstants.Loaders_IDS.STUDENT_ATTENDANCE_LIST,null,this);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentAddAttendantStudentLayout = (ScrollView) rootView.findViewById(R.id.parent_layout_add_student_attendance);
                parentAddAttendantStudentLayout.setVisibility(View.VISIBLE);
                parentListLayout = (CoordinatorLayout) rootView.findViewById(R.id.main_content);
                parentListLayout.setVisibility(View.GONE);
            }
        });
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        studentPresentList = new ArrayList<>();
        studentPresentList.addAll(((ProxyApplication) getActivity().getApplicationContext()).getScannedStudentList());
        adapter = new CustomStudentLIstAdapter(context,studentPresentList);
        recyclerView.setAdapter(adapter);

        addParentAttendantStudentLAyout(rootView);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this.getActivity(),
                AppConstants.CONTENT_URIS.STUDENT_TABLE_URI,
                null,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        super.onLoaderReset(loader);
    }


    private void addParentAttendantStudentLAyout(View rootView) {
        branchSelect = (Spinner) rootView.findViewById(R.id.branch_select);
        YearSelect = (Spinner) rootView.findViewById(R.id.year_select);
        SemesterSelect = (Spinner) rootView.findViewById(R.id.semester_select);
        buttonSave = (AppCompatButton) rootView.findViewById(R.id.save_button);
        nameEditText = (AppCompatEditText) rootView.findViewById(R.id.name_edittext);
        rollNumberText = (AppCompatEditText) rootView.findViewById(R.id.rollnumber_edittext);
        cancelButton = (AppCompatButton) rootView.findViewById(R.id.cancel_button);
        buttonSave.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        branchSelect.setOnItemSelectedListener(this);
        YearSelect.setOnItemSelectedListener(this);
        SemesterSelect.setOnItemSelectedListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:
                if (!TextUtils.isEmpty(branch) && !TextUtils.isEmpty(year)
                        && !TextUtils.isEmpty(semester) && !TextUtils.isEmpty(nameEditText.getText().toString())
                        && !TextUtils.isEmpty(rollNumberText.getText().toString())) {
                    Student studentObject = new Student();
                    studentObject.setName(nameEditText.getText().toString());
                    studentObject.setRollNumber(rollNumberText.getText().toString());
                    ((ProxyApplication) getActivity().getApplicationContext()).addScannedStudentList(studentObject);
                    parentAddAttendantStudentLayout.setVisibility(View.GONE);
                    parentListLayout.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        studentPresentList.add(studentObject);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        nameEditText.setText("");
                        rollNumberText.setText("");
                    }
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter values in all the fields", Toast.LENGTH_SHORT).show();
            break;
            case R.id.cancel_button:
                parentAddAttendantStudentLayout.setVisibility(View.GONE);
                parentListLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
}

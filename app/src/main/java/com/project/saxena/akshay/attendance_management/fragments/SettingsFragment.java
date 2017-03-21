package com.project.saxena.akshay.attendance_management.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.saxena.akshay.attendance_management.AppConstants;
import com.project.saxena.akshay.attendance_management.ProxyApplication;
import com.project.saxena.akshay.attendance_management.R;
import com.project.saxena.akshay.attendance_management.UserAreaActivity;

import java.util.ArrayList;

/**
 * Created by Akshay on 3/17/2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {


    AppCompatButton addSubject,saveSettingsButton;
    AppCompatSpinner subjectListSpinner, YearSpinner, branchSpinner, timeSpinner;
    ArrayAdapter<String> adapter;
    Context mContext;
    ArrayList<String> itemList = null;

    String timeValue, branchSelect, yearSelect, subjectSelect;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.settings_layout, container, false);

        getLoaderManager().restartLoader(AppConstants.Loaders_IDS.SUBJECT_LOADER_ID,null,this);

        addSubject = (AppCompatButton) rootView.findViewById(R.id.add_button);
        saveSettingsButton = (AppCompatButton) rootView.findViewById(R.id.save_settings_button);
        subjectListSpinner = (AppCompatSpinner) rootView.findViewById(R.id.subject_select);
        branchSpinner = (AppCompatSpinner) rootView.findViewById(R.id.branch_select);
        YearSpinner = (AppCompatSpinner) rootView.findViewById(R.id.year_select);
        timeSpinner = (AppCompatSpinner) rootView.findViewById(R.id.time_spinner);

        timeSpinner.setOnItemSelectedListener(this);
        branchSpinner.setOnItemSelectedListener(this);
        YearSpinner.setOnItemSelectedListener(this);
        subjectListSpinner.setOnItemSelectedListener(this);

        addSubject.setOnClickListener(this);
        saveSettingsButton.setOnClickListener(this);

        if (((ProxyApplication) getActivity().getApplicationContext()).
                getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_TIME) != null) {
            timeValue = ((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_TIME).toString();
            timeSpinner.setSelection(getIndex(timeSpinner, timeValue));
        } else
            timeValue = timeSpinner.getSelectedItem().toString();

        if (((ProxyApplication) getActivity().getApplicationContext()).
                getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.BRANCH_NAME) != null) {
            branchSelect = ((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.BRANCH_NAME).toString();
            branchSpinner.setSelection(getIndex(branchSpinner, branchSelect));
        } else
            branchSelect = branchSpinner.getSelectedItem().toString();

        if (((ProxyApplication) getActivity().getApplicationContext()).
                getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.YEAR) != null) {
            yearSelect = ((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.YEAR).toString();
            YearSpinner.setSelection(getIndex(YearSpinner, yearSelect));
        } else
            yearSelect = YearSpinner.getSelectedItem().toString();

        if (((ProxyApplication) getActivity().getApplicationContext()).
                getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_SUBJECT) != null) {
            subjectSelect = ((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_SUBJECT).toString();
            subjectListSpinner.setSelection(getIndex(subjectListSpinner, subjectSelect));
        }

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

    private void showDialog(final boolean isAddedFlow) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Proxy Tracker");
        if (isAddedFlow)
            alert.setMessage("Add Subject");
        else
            alert.setMessage("Do you want to Save the Information ?");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity());

        String okButtonString = null;
        if (isAddedFlow) {
            alert.setView(input);
            okButtonString = "Save";
        }else
            okButtonString = "OK";
        alert.setPositiveButton(okButtonString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (isAddedFlow) {
                    String value = input.getText().toString();
                    if (adapter == null) {
                        setItemsinSpinner(value,subjectListSpinner);
                    } else {
                        itemList.add(value);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ((ProxyApplication) getActivity().getApplicationContext()).
                            getPreferences().setStringAttributes(AppConstants.SharePreferencesConstants.BRANCH_NAME,
                            branchSelect);
                    ((ProxyApplication) getActivity().getApplicationContext()).
                            getPreferences().setStringAttributes(AppConstants.SharePreferencesConstants.YEAR,
                            yearSelect);
                    ((ProxyApplication) getActivity().getApplicationContext()).
                            getPreferences().setStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_SUBJECT,
                            subjectSelect);
                    ((ProxyApplication) getActivity().getApplicationContext()).
                            getPreferences().setStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_TIME,
                            timeValue);
                    Toast.makeText(mContext, "Data Saved Successfully Toast.LENGTH_SHORT",Toast.LENGTH_SHORT).show();
                    ((UserAreaActivity)getActivity()).popupBackStack(false);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void setItemsinSpinner(String value, Spinner object){
        itemList = new ArrayList<>();
        itemList.add(value);
        adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
        object.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_settings_button:
                if (!TextUtils.isEmpty(timeValue) && !TextUtils.isEmpty(branchSelect)
                        && !TextUtils.isEmpty(yearSelect) && !TextUtils.isEmpty(subjectSelect)) {
                   showDialog(false);
                } else {
                    Toast.makeText(mContext, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_button:
                showDialog(true);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.time_spinner) {
            timeValue = parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.branch_select) {
            branchSelect = parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.year_select) {
            yearSelect = parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.subject_select) {
            subjectSelect = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this.getActivity(),
                AppConstants.CONTENT_URIS.SUBJECTS_TABLE_URI,
                null,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0){
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String subject =  cursor.getString(cursor.getColumnIndex(AppConstants.SUBJECTS_TABLE_COLOUMS.SUBJECTS_NAME));
                setItemsinSpinner(subject,subjectListSpinner);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

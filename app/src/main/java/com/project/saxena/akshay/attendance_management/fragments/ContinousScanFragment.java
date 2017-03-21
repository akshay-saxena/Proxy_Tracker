package com.project.saxena.akshay.attendance_management.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.project.saxena.akshay.attendance_management.AppConstants;
import com.project.saxena.akshay.attendance_management.ProxyApplication;
import com.project.saxena.akshay.attendance_management.R;
import com.project.saxena.akshay.attendance_management.ResponseParserManager;
import com.project.saxena.akshay.attendance_management.UserAreaActivity;
import com.project.saxena.akshay.attendance_management.model.InputAttendanceInformation;
import com.project.saxena.akshay.attendance_management.model.Student;

import java.util.List;


/**
 * Created by nitin.saxena on 3/2/2017.
 */

public class ContinousScanFragment extends TabFragment {

    private Context mContext;

    private static final String TAG = ContinousScanFragment.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    boolean isFragmentVisible;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // newInstance constructor for creating fragment with arguments
    public static ContinousScanFragment newInstance(int page, String title) {
        ContinousScanFragment fragmentFirst = new ContinousScanFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    View rootView;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            if (((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getLongAttributes(AppConstants.SharePreferencesConstants.SAVED_DATE) == null || (System.currentTimeMillis() - ((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getLongAttributes(AppConstants.SharePreferencesConstants.SAVED_DATE) > 43200000))// 12hours conversion
                ((ProxyApplication) getActivity().getApplicationContext()).
                        getPreferences().setLongAttributes(AppConstants.SharePreferencesConstants.SAVED_DATE, System.currentTimeMillis());

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) rootView.findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));

            Student stObject = new ResponseParserManager().parseResponse(result.getText());
            stObject.setPursuingYear(((ProxyApplication) mContext.getApplicationContext())
                    .getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.YEAR));
            stObject.setBranch(((ProxyApplication) mContext.getApplicationContext())
                    .getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.BRANCH_NAME));
            stObject.setCurrentSubject(((ProxyApplication) mContext.getApplicationContext())
                    .getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_SUBJECT));
            ((ProxyApplication) mContext.getApplicationContext()).addScannedStudentList(stObject);
            Log.d(TAG, "***List Size of Scanned Students****" +
                    ((ProxyApplication) mContext.getApplicationContext()).getScannedStudentList().size());
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.continuous_scan, container, false);
        barcodeView = (DecoratedBarcodeView) rootView.findViewById(R.id.barcode_scanner);
        return rootView;
    }


    private boolean isPopUpShown() {
        String yearSelect = ((ProxyApplication) getActivity().getApplicationContext()).
                getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.YEAR);
        String branchSelect = ((ProxyApplication) getActivity().getApplicationContext()).
                getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.BRANCH_NAME);
        String subjectSelect = ((ProxyApplication) getActivity().getApplicationContext()).
                getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_SUBJECT);
        if (!TextUtils.isEmpty(yearSelect) && !(TextUtils.isEmpty(branchSelect))
                && !(TextUtils.isEmpty(subjectSelect)))
            return false;
        else
            return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("NITIN", "******DEstroy View of Continoud Fragment");
        barcodeView.pause();
    }

    protected void showErrorDialog(boolean isPopupShown) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (isPopupShown) {
            builder.setMessage("Please select the Attendance Information Data.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            ((UserAreaActivity) getActivity()).setFragment(false, new SettingsFragment(), "settings_fragment");
                            //  ((UserAreaActivity) getActivity()).popupBackStack(false);

                        }
                    });
               /* .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/
        } else {
            builder.setMessage("PLaceHOlder if Date Chnages and List is not yet saved")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
               /* .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("NITIN SAXENA", "VISIBLE OR NOT::::" + isVisibleToUser);
        isFragmentVisible = isVisibleToUser;
        if (isVisibleToUser) {
                if (!isPopUpShown()) {
                    if (barcodeView != null)
                        barcodeView.decodeContinuous(callback);
                    beepManager = new BeepManager((Activity) mContext);
                    Log.d("NITIN", "*****ContinousScanFragment***");
                } else {
                    showErrorDialog(true);
                }
        }
    }
}

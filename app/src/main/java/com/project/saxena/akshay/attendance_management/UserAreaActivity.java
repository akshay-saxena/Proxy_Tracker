package com.project.saxena.akshay.attendance_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.saxena.akshay.attendance_management.activity.BaseActivity;
import com.project.saxena.akshay.attendance_management.activity.LoginActivity;
import com.project.saxena.akshay.attendance_management.fragments.SettingsFragment;
import com.project.saxena.akshay.attendance_management.fragments.TabFragment;
import com.project.saxena.akshay.attendance_management.model.DBManager;
import com.project.saxena.akshay.attendance_management.model.InputAttendanceInformation;
import com.project.saxena.akshay.attendance_management.model.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UserAreaActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth firebaseAuth;
    private TextView useremail, username;
    private Button ButtonScanning;
    DrawerLayout drawer;
    protected String userDate1;
    //private ImageView Userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        useremail = (TextView) headerView.findViewById(R.id.UserEmailid);
        username = (TextView) headerView.findViewById(R.id.UserName);
        //Userimage = (ImageView) headerView.findViewById(R.id.imageView);
        ButtonScanning = (Button) findViewById(R.id.Scanning);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            useremail.setText(user.getEmail());
            username.setText(user.getDisplayName());
        }
      /*  Intent intent=getIntent();
        useremail.setText(intent.getStringExtra("EmailUser"));
        username.setText(intent.getStringExtra("DisplayName"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("UserImage"))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Userimage);*/

/*
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(UserAreaActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };*/
        /*ButtonScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, captureActivity.class));
                finish();
            }
        });*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            setFragment(false, new TabFragment(), "tabFragmentScan_attendance");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            popupBackStack(false);
        else
            showExitAlert();
    }

    public void setFragment(boolean isAdded, Fragment fragment, String fragmentTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isAdded)
            transaction.add(R.id.container, fragment,fragmentTag).addToBackStack(fragmentTag);
        else
            transaction.replace(R.id.container, fragment,fragmentTag).addToBackStack(fragmentTag);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_area, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            executePushDatatoDatabaseEntry();
            return true;
        }else if(id == R.id.action_sync){
            //TODO AKSHAY
        }
        return super.onOptionsItemSelected(item);
    }

    public void executePushDatatoDatabaseEntry(){
       showProgressDialog("Inserting Records,Please wait..");
            new DBManager(this).
                    bulkInsertStudentRecords(new ArrayList<Student>(
                            ((ProxyApplication)getApplicationContext()).getScannedStudentList()));
        ((ProxyApplication)getApplicationContext()).deletewholeDatafromScannedList();
        ((TabFragment)getSupportFragmentManager().findFragmentByTag("tabFragmentScan_attendance")).notifyDataSetChangedAttendanceListAdapter();
         hideProgressDialog();
    }


    public void clearPreference(){
        ((ProxyApplication) this.getApplicationContext()).
                getPreferences().setStringAttributes("year",null);
        ((ProxyApplication) this.getApplicationContext()).
                getPreferences().setStringAttributes("branch",null);
        ((ProxyApplication) this.getApplicationContext()).
                getPreferences().setStringAttributes("semester",null);
        ((ProxyApplication) this.getApplicationContext()).
                getPreferences().setLongAttributes("datesaved",0);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment frag = null;
                String tagName = null;
                popupBackStack(true);
                switch (item.getItemId()) {
                    case R.id.scan_attendance_info:
                        frag = new TabFragment();
                        tagName = "tabFragmentScan_attendance";
                        break;
                    case R.id.view_update:
                        frag = new InputAttendanceInformation();
                        tagName = "inputAttendanceInfo_fragment";
                        break;
                    case R.id.logout:
                        logout();
                        break;
                    case R.id.nav_settings:
                        frag = new SettingsFragment();
                        tagName = "settings_fragment";
                        break;
                }

                    if (frag != null) {
                        setFragment(false, frag, tagName);
                        drawer.closeDrawers();
                        return true;
                    }

                return false;

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {

        firebaseAuth.getInstance().signOut();
        clearPreference();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            startActivity(new Intent(UserAreaActivity.this, LoginActivity.class));
                            finish();
                        } else
                            Toast.makeText(UserAreaActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void popupBackStack(boolean isMakeStackEmpty) {
        if (isMakeStackEmpty) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().executePendingTransactions();
        }

    }

    protected void showExitAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



}



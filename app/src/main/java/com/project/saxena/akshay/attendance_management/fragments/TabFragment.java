package com.project.saxena.akshay.attendance_management.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.saxena.akshay.attendance_management.AppConstants;
import com.project.saxena.akshay.attendance_management.ProxyApplication;
import com.project.saxena.akshay.attendance_management.R;
import com.project.saxena.akshay.attendance_management.UserAreaActivity;

/**
 * Created by nitin.saxena on 3/2/2017.
 */

public class TabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    FragmentStatePagerAdapter adapterViewPager;
    UserAreaActivity mContext;

    TextView textView;

    @Override
    public void onAttach(Context context) {
        mContext = (UserAreaActivity) context;
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_carousel, container, false);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Scan QrCode"));
        tabLayout.addTab(tabLayout.newTab().setText("Scanned List"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        textView = (TextView) rootView.findViewById(R.id.information_textbox);


        if(TextUtils.isEmpty(textView.getText().toString()))
        {
            textView.setText("Please Select your Lecture information by selecting Profile Option...");
        }else {
            textView.setText("Year: "+((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.YEAR)
                            +"   Branch: "+ ((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.BRANCH_NAME)
                    +"   Subject: "+ ((ProxyApplication) getActivity().getApplicationContext()).
                    getPreferences().getStringAttributes(AppConstants.SharePreferencesConstants.SELECTED_SUBJECT));
        }
        textView.setSelected(true);  // Set focus to the textview

        final ViewPager vpPager = (ViewPager) rootView.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(mContext.getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("NITIN", "***POSITION***" + position);
                if (position == 1)
                    adapterViewPager.notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    private void getSubjects() {
        getActivity().getContentResolver().query(AppConstants.CONTENT_URIS.SUBJECTS_TABLE_URI,null,null,null,null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static class MyPagerAdapter extends FragmentStatePagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return ContinousScanFragment.newInstance(0, "Scan");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return CurrentStudentListFragment.newInstance(1, "AttendanceList");
                //return InputAttendanceInformation.newInstance(1, "AttendanceList");
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(View container, int position) {
            return super.instantiateItem(container, position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }


    public void notifyDataSetChangedAttendanceListAdapter(){
        CurrentStudentListFragment fragment = (CurrentStudentListFragment)mContext.
                getSupportFragmentManager().findFragmentByTag("AttendanceList");
        fragment.adapter.notifyDataSetChanged();
    }
}

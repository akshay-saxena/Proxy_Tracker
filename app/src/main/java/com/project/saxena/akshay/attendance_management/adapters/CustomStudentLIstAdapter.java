package com.project.saxena.akshay.attendance_management.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.saxena.akshay.attendance_management.AppConstants;
import com.project.saxena.akshay.attendance_management.ProxyApplication;
import com.project.saxena.akshay.attendance_management.R;
import com.project.saxena.akshay.attendance_management.model.Student;

import java.util.ArrayList;

import static android.view.LayoutInflater.from;

/**
 * Created by nitin.saxena on 3/5/2017.
 */

public class CustomStudentLIstAdapter extends RecyclerView.Adapter<CustomStudentLIstAdapter.MyViewHolder> {
    protected ArrayList<Student> dataSet;
    Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        TextView textViewBranch;
        TextView textViewYear;
        ImageView imageViewIcon;



        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.textViewBranch = (TextView) itemView.findViewById(R.id.branch_name_list);
            this.textViewYear = (TextView) itemView.findViewById(R.id.year_name_list);
           // this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public CustomStudentLIstAdapter(Context context , ArrayList<Student> data) {
        this.dataSet = data;
        mContext =  context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

       /* view.setOnClickListener(MainActivity.myOnClickListener);*/

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        TextView textViewYear = holder.textViewYear;
        TextView textViewBranch = holder.textViewBranch;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getRollNumber());
        textViewBranch.setText("("+((ProxyApplication)mContext.getApplicationContext()).getPreferences()
        .getStringAttributes(AppConstants.SharePreferencesConstants.BRANCH_NAME)+")");
        textViewYear.setText(((ProxyApplication)mContext.getApplicationContext()).getPreferences()
                .getStringAttributes(AppConstants.SharePreferencesConstants.YEAR));
        //imageView.setImageResource(dataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        if (dataSet != null)
            return dataSet.size();
        else
            return 0;
    }
}

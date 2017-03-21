package com.project.saxena.akshay.attendance_management.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.project.saxena.akshay.attendance_management.R;

public class Frogotpassword extends Activity {
    EditText editText;
    FirebaseAuth firebaseAuth;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frogotpassword);
        button=(Button)findViewById(R.id.forgotbutton);
        editText=(EditText)findViewById(R.id.forgotemail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editText.getText().toString();
                if (email.matches("")) {
                    editText.setError("Enter Email Address");
                    return;
                }
                firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Email Successfully Sent !",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Frogotpassword.this,LoginActivity.class));
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Error Occurred !",Toast.LENGTH_LONG).show();
                        editText.requestFocus();
                    }
                });
            }
        });
    }
}

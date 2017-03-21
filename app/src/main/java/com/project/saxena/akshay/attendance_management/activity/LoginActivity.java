
package com.project.saxena.akshay.attendance_management.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.saxena.akshay.attendance_management.R;
import com.project.saxena.akshay.attendance_management.UserAreaActivity;
import com.project.saxena.akshay.attendance_management.Utils;

public class LoginActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final String USERNAME = "username";
    private static final String EMAIL_ID = "emailId";
    private static final String PHOTO_URL = "photoUrl";


    FirebaseAuth firebaseAuth;
    private EditText inputEmail, inputPassword;
  //  private GoogleApiClient googleApiClient;
    private Button registerButton, forgotbutton, loginbutton;
    private SignInButton googleSign;
    private ProgressBar progressBar;


    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } /*else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, UserAreaActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected)
            Toast.makeText(getApplicationContext(), "No Internet Available", Toast.LENGTH_LONG);


        inputEmail = (EditText) findViewById(R.id.loginemail);
        inputPassword = (EditText) findViewById(R.id.loginpassword);
        registerButton = (Button) findViewById(R.id.registerbutton);
        loginbutton = (Button) findViewById(R.id.loginbutton);
        forgotbutton = (Button) findViewById(R.id.forgotbutton);
        progressBar = (ProgressBar) findViewById(R.id.loginprogressBar);
        googleSign = (SignInButton) findViewById(R.id.sign_in_button);
        googleSign.setSize(SignInButton.SIZE_STANDARD);
        googleSign.setOnClickListener(this);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                if (email.matches("")) {
                    inputEmail.setError("Enter Email Address");
                    return;
                }
                final String password = inputPassword.getText().toString();
                if (password.matches("")) {
                    inputPassword.setError("Enter Password");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else
                                Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    }
                });

            }
        });
        forgotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Frogotpassword.class));
            }
        });
    }




    public void toRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterAcitivity.class);
        startActivity(intent);
    }

    public void initiateGoogleSignIn() {
        if(Utils.isInternetConnected(this)) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                initiateGoogleSignIn();

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
                firebaseAuthWithGoogle(result);

        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInResult result) {
        Log.d(TAG, "firebase Auth with google " + result.getSignInAccount().getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(result.getSignInAccount().getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        handleSignInResult(result);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
/*
            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

               *//* txtName.setText(personName);
                txtEmail.setText(email);*//*
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            //.into(imgProfilePic);*/

            updateUI(true,result);
        } else {
            // Signed out, show unauthenticated UI.
            Log.e(TAG, "Unauthenticated");
            updateUI(false,null);
        }
    }

    private void updateUI(boolean isSignedIn,GoogleSignInResult result) {
        if (isSignedIn) {
            Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
            intent.putExtra(EMAIL_ID,result.getSignInAccount().getEmail());
            intent.putExtra(USERNAME,result.getSignInAccount().getDisplayName());
            intent.putExtra(PHOTO_URL,result.getSignInAccount().getPhotoUrl().toString());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "PLease try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


}

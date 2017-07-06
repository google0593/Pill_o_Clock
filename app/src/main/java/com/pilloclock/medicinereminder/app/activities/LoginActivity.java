package com.pilloclock.medicinereminder.app.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pilloclock.medicinereminder.app.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "MAIN_ACTIVITY";

    private FirebaseAuth mFirebaseAuth;

    public static String userName;
    public static Uri userPhoto;
    public static String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        //enable firebase offline/disk persistence

        setContentView(R.layout.activity_main);

        isUserLoggedIn();

        findViewById(R.id.logout_button).setOnClickListener(this);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("hash key", something);
            }
        }
        catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            Log.e("name not found", e1.toString());
        }

        catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e){
            Log.e("exception", e.toString());
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: USER IS LOGGED IN:" + mFirebaseAuth.getCurrentUser().getEmail());
                getDetails();

                Intent main_activity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(main_activity);
                finish();

            } else {
                Log.d(TAG, "onActivityResult: NO USER FOUND!");
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout_button) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "onComplete: USER LOGGED OUT!!");
                            finish();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void isUserLoggedIn() {
        //check if user is already logged in.
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null) {

            Log.d(TAG, "onCreate: User is already signed in!");
            Log.d(TAG, "onCreate: USER IS LOGGED IN:" + mFirebaseAuth.getCurrentUser().getEmail());
            Log.d(TAG, "onCreate: " + userName);

            getDetails();
            //Uncomment if you want to start a new activity/intent
            Intent main_activity = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main_activity);
            finish();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.mipmap.ic_launcher)
                            .setTheme(R.style.FirebaseLoginTheme)
                            //ToDo smart lock needs to be removed or set to true before publishing
                            .setIsSmartLockEnabled(false)
                            .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }


    private void getDetails() {

        userName = mFirebaseAuth.getCurrentUser().getDisplayName();
        userPhoto = mFirebaseAuth.getCurrentUser().getPhotoUrl();
        userID = mFirebaseAuth.getCurrentUser().getUid();
        Log.d(TAG, "getDetails: " + userPhoto);
    }


    private void setFullScreen(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
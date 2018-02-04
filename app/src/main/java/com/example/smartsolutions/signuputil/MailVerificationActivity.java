package com.example.smartsolutions.signuputil;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.smartsolutions.loginutil.LoginActiviy;
import com.example.smartsolutions.smartschool.R;
import com.example.smartsolutions.teacherutil.TeacherHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by mmenem on 03/02/18.
 */

public class MailVerificationActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_verification);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = mAuth.getCurrentUser();

                if (user != null) {
                    userID = user.getUid();

                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Intent openHomeActivity = new Intent(MailVerificationActivity.this, TeacherHomeActivity.class);
                                startActivity(openHomeActivity);

                            } else {
                                overridePendingTransition(0, 0);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());

                            }

                        }
                    });

                } else {
                    Intent loginActivity = new Intent(MailVerificationActivity.this, LoginActiviy.class);
                    startActivity(loginActivity);
                }
            }
        };


    }
}

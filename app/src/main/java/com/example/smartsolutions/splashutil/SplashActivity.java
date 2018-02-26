package com.example.smartsolutions.splashutil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.smartsolutions.loginutil.LoginActiviy;
import com.example.smartsolutions.signuputil.SignupActivity;
import com.example.smartsolutions.smartschool.R;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginActivity=new Intent(SplashActivity.this,LoginActiviy.class);

                Toast.makeText(SplashActivity.this, "kh", Toast.LENGTH_SHORT).show();
                startActivity(loginActivity);
//                Intent signupActivity=new Intent(SplashActivity.this,SignupActivity.class);
//
//
//                startActivity(loginActivity);
            }
        }, 5000);

    }
}
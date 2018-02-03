package com.example.smartsolutions.loginutil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartsolutions.signuputil.SignupActivity;
import com.example.smartsolutions.smartschool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mmenem on 31/01/18.
 */

public class LoginActiviy extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView signUpText;
    private ProgressDialog pDialog;
    private FirebaseAuth mAuth;
    private String email, password;
    private View focusView = null;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_pass);
        signUpText = (TextView) findViewById(R.id.link_to_register);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        pDialog = new ProgressDialog(this);
        btnLogin.setOnClickListener(this);
        signUpText.setOnClickListener(this);


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                validateUserInputs();
                break;
            case R.id.link_to_register:
                openSignupActivity();
                break;

        }
    }

    private void openSignupActivity() {
        Intent signupActivity = new Intent(LoginActiviy.this, SignupActivity.class);

        startActivity(signupActivity);
    }

    private void validateUserInputs() {
        if (!valid()) {
            focusView.requestFocus();

        } else {
            pDialog.setMessage(String.valueOf(getApplicationContext().getResources().getString(R.string.logging_in)));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            login(email, password);
        }

    }

    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }


    private boolean valid() {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getText(R.string.signup_email_validation_empty_email));
            focusView = etEmail;
            return false;
        } else if (!isEmailValid(email)) {
            etEmail.setError(getText(R.string.signup_email_validation_invalid_email));
            focusView = etEmail;
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getText(R.string.signup_password_validation_empty_password));
            focusView = etPassword;
            return false;
        }
        return true;
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            pDialog.dismiss();
                            Toast.makeText(LoginActiviy.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            pDialog.dismiss();
                            Toast.makeText(LoginActiviy.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }


    private void openLoginActivity() {
        // Intent loginActivity=new Intent(SplashActivity.this,LoginActiviy.class);
    }
}
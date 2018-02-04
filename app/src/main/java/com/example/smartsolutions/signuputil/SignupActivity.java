package com.example.smartsolutions.signuputil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartsolutions.smartschool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Signup Activity";
    private static final int GALLERY_REQUEST = 999;
    private EditText etName, etUsername, etPhone, etEmail, etPassword, etSchool;
    private Button btnSignup;
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;
    private RadioButton userChoice;
    private TextView tvUploadProfileImage;
    private String name, username, phone, email, password, school, grade, userChoiceText = "Student";
    private View focusView = null;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private ProgressDialog pDialog;
    private Uri photoUri = null;
    private FirebaseAuth mAuth;
    private DatabaseReference schoolDatabase;
    private StorageReference mStorageImages;
    private RadioButton radioStudent, radioTeacher, radioGrade1, radioGrade2, radioGrade3, radioGrade4;
    private LinearLayout gradesOfTeacher, gradesOfStudent, coursesBar;
    private String userID;
    private CheckBox checkboxGrade1, checkBoxGrade2, checkBoxGrade3, checkBoxGrade4, checkboxArabicCourse, checkBoxEnglishCourse, checkBoxMathCourse, checkBoxScienceCourse;
    private String choicenGrade = "Grade1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        coursesBar = (LinearLayout) findViewById(R.id.courses_bar);
        gradesOfTeacher = (LinearLayout) findViewById(R.id.grades_of_teacher);
        gradesOfStudent = (LinearLayout) findViewById(R.id.grades_of_student);
        etName = (EditText) findViewById(R.id.et_name);
        etUsername = (EditText) findViewById(R.id.et_userName);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        etSchool = (EditText) findViewById(R.id.et_school);
        btnSignup = (Button) findViewById(R.id.btnsignup);
        tvUploadProfileImage = (TextView) findViewById(R.id.tv_upload);
        circleImageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.iv_profile);
        radioStudent = (RadioButton) findViewById(R.id.radio_student);
        radioTeacher = (RadioButton) findViewById(R.id.radio_teacher);
        radioGrade1 = (RadioButton) findViewById(R.id.grade1_student);
        radioGrade2 = (RadioButton) findViewById(R.id.grade2_student);
        radioGrade3 = (RadioButton) findViewById(R.id.grade3_student);
        radioGrade4 = (RadioButton) findViewById(R.id.grade4_student);
        checkboxGrade1 = (CheckBox) findViewById(R.id.teacher_grades_1);
        checkBoxGrade2 = (CheckBox) findViewById(R.id.teacher_grades_2);
        checkBoxGrade3 = (CheckBox) findViewById(R.id.teacher_grades_3);
        checkBoxGrade4 = (CheckBox) findViewById(R.id.teacher_grades_4);
        checkboxArabicCourse = (CheckBox) findViewById(R.id.checkbox_arabic_course);
        checkBoxEnglishCourse = (CheckBox) findViewById(R.id.checkbox_english_course);
        checkBoxMathCourse = (CheckBox) findViewById(R.id.checkbox_math_course);
        checkBoxScienceCourse = (CheckBox) findViewById(R.id.checkbox_science_course);

        circleImageView.setOnClickListener(this);
        tvUploadProfileImage.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        pDialog = new ProgressDialog(this);
        radioStudent.setOnClickListener(this);
        radioTeacher.setOnClickListener(this);
        radioGrade1.setOnClickListener(this);
        radioGrade2.setOnClickListener(this);
        radioGrade3.setOnClickListener(this);
        radioGrade4.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_profile:
                openPhotoIntent();
                break;
            case R.id.tv_upload:
                openPhotoIntent();
                break;
            case R.id.btnsignup:
                validateUserInputs();
                break;

            case R.id.radio_student:
                if (radioStudent.isChecked())
                    gradesOfTeacher.setVisibility(View.GONE);
                gradesOfStudent.setVisibility(View.VISIBLE);
                userChoiceText = "Student";
                break;
            case R.id.radio_teacher:
                if (radioTeacher.isChecked())
                    userChoiceText = "Teacher";
                gradesOfStudent.setVisibility(View.GONE);
                gradesOfTeacher.setVisibility(View.VISIBLE);
                break;
            case R.id.grade1_student:
                choicenGrade = "Grade1";
                break;
            case R.id.grade2_student:
                choicenGrade = "Grade2";
                break;
            case R.id.grade3_student:
                choicenGrade = "Grade3";
                break;
            case R.id.grade4_student:
                choicenGrade = "Grade4";
                break;


        }
    }


    private void openPhotoIntent() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, GALLERY_REQUEST);

        }

    }

    private void validateUserInputs() {
        if (!valid()) {
            focusView.requestFocus();

        } else {
            pDialog.setMessage(String.valueOf(getApplicationContext().getResources().getString(R.string.signing_up)));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            signupNewUser(email, password);
        }

    }


    private void signupNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            if (photoUri != null) {
                                updateUserInfoWithphoto(name, username, phone, school, photoUri);
                            } else {
                                updateUserInfoWithoutPhoto(name, username, phone, school);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }

                    }
                });
    }

    private void updateUserInfoWithoutPhoto(String name, String username, String phone, String school) {
        if (userChoiceText.equals("Teacher")) {
            schoolDatabase = FirebaseDatabase.getInstance().getReference().child("school_names").child(school).child("school_staff").child("teachers");
            ArrayList<String> subjectList = getSubjectList();
            ArrayList<String> gradesList = getGradesList();
            TeacherInformation teacherInformation = new TeacherInformation(name, username, phone, email, gradesList, subjectList, userChoiceText);
            schoolDatabase.child(userID).setValue(teacherInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignupActivity.this, "Signed-up successfully", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    Intent i = new Intent(SignupActivity.this, MailVerificationActivity.class);
                    startActivity(i);

                }
            });
        } else {
            schoolDatabase = FirebaseDatabase.getInstance().getReference().child("school_names").child(school).child("grades").child(choicenGrade).child("students");
            ArrayList<String> subjectList = getSubjectList();
            StudentInformation studentInformation = new StudentInformation(name, username, phone, email, choicenGrade, subjectList, userChoiceText);
            schoolDatabase.child(userID).setValue(studentInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignupActivity.this, "Signed-up successfully", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    Intent i = new Intent(SignupActivity.this, MailVerificationActivity.class);
                    startActivity(i);

                }
            });
        }

    }

    private ArrayList<String> getSubjectList() {
        ArrayList<String> subjectChoices = new ArrayList<>();

        if (checkboxArabicCourse.isChecked()) {
            subjectChoices.add("Arabic");
        }
        if (checkBoxEnglishCourse.isChecked()) {
            subjectChoices.add("English");
        }
        if (checkBoxMathCourse.isChecked()) {
            subjectChoices.add("Math");
        }
        if (checkBoxScienceCourse.isChecked()) {
            subjectChoices.add("Science");
        }

        return subjectChoices;


    }

    private ArrayList<String> getGradesList() {
        ArrayList<String> gradesChoices = new ArrayList<>();
        if (checkboxGrade1.isChecked()) {
            gradesChoices.add("grade1");
        }
        if (checkBoxGrade2.isChecked()) {
            gradesChoices.add("grade2");
        }
        if (checkBoxGrade3.isChecked()) {
            gradesChoices.add("grade3");
        }
        if (checkBoxGrade4.isChecked()) {
            gradesChoices.add("grade4");
        }

        return gradesChoices;

    }

    private void updateUserInfoWithphoto(final String name, final String username, final String phone, final String school, Uri photoUri) {
        if (userChoiceText.equals("Teacher")) {

            mStorageImages = FirebaseStorage.getInstance().getReference().child("profile_images");

            StorageReference filePath = mStorageImages.child(photoUri.getLastPathSegment());
            filePath.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    schoolDatabase = FirebaseDatabase.getInstance().getReference().child("school_names").child(school).child("school_staff").child("teachers");
                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    ArrayList<String> subjectList = getSubjectList();
                    ArrayList<String> gradesList = getGradesList();
                    TeacherInformation teacherInformation = new TeacherInformation(name, username, phone, email, gradesList, subjectList, downloadUri, userChoiceText);
                    schoolDatabase.child(userID).setValue(teacherInformation);
                    pDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignupActivity.this, MailVerificationActivity.class);
                    startActivity(i);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ArrayList<String> subjectList = getSubjectList();
                    ArrayList<String> gradesList = getGradesList();
                    TeacherInformation teacherInformation = new TeacherInformation(name, username, phone, email, gradesList, subjectList, userChoiceText);
                    schoolDatabase.child(userID).setValue(teacherInformation);
                    pDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignupActivity.this, MailVerificationActivity.class);
                    startActivity(i);
                }
            });


        } else {

            mStorageImages = FirebaseStorage.getInstance().getReference().child("profile_images");

            StorageReference filePath = mStorageImages.child(photoUri.getLastPathSegment());
            filePath.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    schoolDatabase = FirebaseDatabase.getInstance().getReference().child("school_names").child(school).child("grades").child(choicenGrade).child("students");
                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    Log.d("downloadUri", downloadUri);
                    ArrayList<String> subjectList = getSubjectList();
                    StudentInformation studentInformation = new StudentInformation(name, username, phone, email, choicenGrade, subjectList, downloadUri, userChoiceText);
                    schoolDatabase.child(userID).setValue(studentInformation);
                    pDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignupActivity.this, MailVerificationActivity.class);
                    startActivity(i);


                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ArrayList<String> subjectList = getSubjectList();
                    StudentInformation studentInformation = new StudentInformation(name, username, phone, email, choicenGrade, subjectList, userChoiceText);
                    schoolDatabase.child(userID).setValue(studentInformation);
                    pDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "failed to upload the image!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignupActivity.this, MailVerificationActivity.class);
                    startActivity(i);

                }
            });
        }

    }


    private boolean valid() {
        school = etSchool.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        username = etUsername.getText().toString().trim();
        name = etName.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            etName.setError(getText(R.string.signup_name_validation_empty_name));
            focusView = etName;
            return false;
        }
        if (TextUtils.isEmpty(username)) {
            etUsername.setError(getText(R.string.signup_username_validation_empty_username));

        }
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
        } else if (password.length() < 8) {
            etPassword.setError(getText(R.string.signup_password_validation_invalid_password));
            focusView = etPassword;
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError(getText(R.string.signup_phone_validation_empty_phone));
            focusView = etPhone;
            return false;
        } else if (!isPhoneValid(phone)) {
            etPhone.setError(getText(R.string.signup_phone_validation_invalid_phone));
            focusView = etPhone;
            return false;
        }


        if (TextUtils.isEmpty(school)) {
            etSchool.setError(getText(R.string.signup_school_validation_empty_school));
            focusView = etSchool;
            return false;
        }
        return true;
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() > 5 && phone.length() < 28;

    }

    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAutoZoomEnabled(true)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoUri = result.getUri();
                circleImageView.setImageURI(photoUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Failed to upload the photo", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
package com.example.shapelyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shapelyapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;


public class SigninActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText singin_EDT_username;
    private EditText singin_EDT_age;
    private EditText singin_EDT_height;
    private EditText singin_EDT_weight;
    private EditText singin_EDT_email;
    private EditText signin_EDT_password;

    private Spinner singin_SNR_Usertype;

    private CheckBox singin_CB_male;
    private CheckBox singin_CB_female;

    private Button singin_BTN_singin;
    private Button singin_BTN_back;

    String userId;
    private DatabaseReference db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        findViews();
        db = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ArrayAdapter<CharSequence> userAdapter = ArrayAdapter.createFromResource(this, R.array.users_arrays, android.R.layout.simple_spinner_item);
        userAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        singin_SNR_Usertype.setAdapter(userAdapter);
        singin_SNR_Usertype.setOnItemSelectedListener(this);

        singin_BTN_singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });

        singin_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        singin_CB_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (singin_CB_male.isChecked()) {
                    singin_CB_female.setChecked(false);
                }
            }
        });

        singin_CB_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (singin_CB_female.isChecked()) {
                    singin_CB_male.setChecked(false);
                }
            }
        });
    }

    // Insert new user details
    private void RegisterUser() {
        // Get users data
        final String userName = singin_EDT_username.getText().toString().trim();
        final String userAge = singin_EDT_age.getText().toString().trim();
        final String userHeight = singin_EDT_height.getText().toString().trim();
        final String userWeight = singin_EDT_weight.getText().toString().trim();
        final String userEmail = singin_EDT_email.getText().toString().trim();
        String password = signin_EDT_password.getText().toString().trim();
        final String userType = singin_SNR_Usertype.getSelectedItem().toString().trim();
        final String male = singin_CB_male.getText().toString().trim();
        final String female = singin_CB_female.getText().toString().trim();


        // Check if there are errors and return if there is
        if (checkErrors(userName,
                userAge,
                userHeight,
                userWeight,
                userEmail,
                password,
                userType)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            User user = new User(
                                    userName, userAge, Double.parseDouble(userHeight), Double.parseDouble(userWeight),
                                    userEmail, userType, male != null ? male : female
                            );

                            db.child("users")
                                    .child(UUID.randomUUID().toString())
                                    .setValue(user);

                            Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.i("TAG", "Create user with email encounter a problem: ");
                        }
                    }

                });
    }

    // Check if there are errors and return true if there is
    private boolean checkErrors(String name, String age, String height, String weight, String
            email, String password, String userType) {

        // Check if there is no empty value
        if (checkEmpties(name, age, height, weight, email, password, userType)) {
            return true;
        }

        // Validate password length
        if (password.length() < 6) {
            signin_EDT_password.setError("Password Must include at least 6 characters. ");
            return true;
        } else {
            return false;
        }
    }

    private boolean checkEmpties(String name, String age, String height, String weight, String
            email, String password, String userType) {
        boolean to_return = false;


        if (checkEmptyTxt(name, singin_EDT_username, "Name ")) {
            to_return = true;
        }
        if (checkEmptyTxt(age, singin_EDT_age, "age")) {
            to_return = true;
        }
        if (checkEmptyTxt(height, singin_EDT_height, "Height ")) {
            to_return = true;
        }
        if (checkEmptyTxt(weight, singin_EDT_weight, "Weight ")) {
            to_return = true;
        }
        if (checkEmptyTxt(email, singin_EDT_email, "Email ")) {
            to_return = true;
        }
        if (checkEmptyTxt(password, signin_EDT_password, "Password ")) {
            to_return = true;
        }

        if (checkEmptySpinner(userType, singin_SNR_Usertype)) {
            to_return = true;
        }

        return to_return;
    }

    // Check if edit text is empty
    private boolean checkEmptyTxt(String str, EditText editText, String missing) {
        if (TextUtils.isEmpty(str)) {
            editText.setError(missing + "is Required. ");
            return true;
        }
        return false;
    }

    private boolean checkEmptySpinner(String str, Spinner spinner) {
        if (spinner.getSelectedItem().toString().equals("user type")) {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setError("");
            errorText.setText("user type is Required. ");
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }

    private void findViews() {
        singin_EDT_username = findViewById(R.id.singin_EDT_username);
        singin_EDT_age = findViewById(R.id.singin_EDT_age);
        singin_EDT_height = findViewById(R.id.singin_EDT_height);
        singin_EDT_weight = findViewById(R.id.singin_EDT_weight);
        singin_EDT_email = findViewById(R.id.singin_EDT_email);
        signin_EDT_password = findViewById(R.id.signin_EDT_password);
        singin_SNR_Usertype = findViewById(R.id.singin_SNR_Usertype);
        singin_CB_male = findViewById(R.id.singin_CB_male);
        singin_CB_female = findViewById(R.id.singin_CB_female);
        singin_BTN_singin = findViewById(R.id.singin_BTN_singin);
        singin_BTN_back = findViewById(R.id.singin_BTN_back);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

}
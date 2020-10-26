package com.example.shapelyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shapelyapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private EditText login_EDT_email;
    private EditText login_EDT_password;
    private Button login_BTN_login;
    private  Button login_BTN_back;

    private DatabaseReference databaseReference;
    private User currUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        findView();
        mAuth = FirebaseAuth.getInstance();
        login_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn();
            }
        });


        login_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findView() {
        login_EDT_email = findViewById(R.id.login_EDT_email);
        login_EDT_password = findViewById(R.id.login_EDT_password);
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_BTN_back= findViewById(R.id.login_BTN_back);
    }

    private void readCurrentUserFromDB(final String userEmail) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue(User.class).getEmail().equals(userEmail)) {
                        currUser = dataSnapshot.getValue(User.class);
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        intent.putExtra(getString(R.string.user_id), currUser);
                        startActivity(intent);
                        finish();
                    }else{
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LogIn() {
        // Get users data
        final String email = login_EDT_email.getText().toString().trim();
        String password = login_EDT_password.getText().toString().trim();

        // Check if there are errors and return if there is
        if (checkErrors(email, password)) {
            Log.i("TAG", "LogIn: Error has occurred");
            return;
        }else{

        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    readCurrentUserFromDB(email);
                } else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("The password or email is incorrect")
                            .setContentText("Try again")
                            .show();
                }
            }
        });
    }

    // Check if there are errors and return true if there is
    boolean checkErrors(String email, String password) {

        if (checkEmpties(email, password)) {
            return true;
        }
        // Validate password length
        if (password.length() < 6) {
            login_EDT_password.setError("Password Must include at least 6 characters. ");
            return true;
        }
        return false;
    }

    // Check if email or password was not insert
    boolean checkEmpties(String email, String password) {
        boolean to_return = false;

        if (checkEmptyTxt(email, login_EDT_email, "User email")) {
            to_return = true;
        }
        if (checkEmptyTxt(password, login_EDT_password, "Password")) {
            to_return = true;
        }
        return to_return;
    }

    // Check if edit test is empty
    boolean checkEmptyTxt(String str, EditText editText, String missing) {
        if (TextUtils.isEmpty(str)) {
            editText.setError(missing + "is Required. ");
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Log.i("TAG", "onStart: " + currentUser.getEmail());
        }
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

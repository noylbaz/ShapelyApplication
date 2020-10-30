package com.example.shapelyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class WorkoutActivity extends AppCompatActivity {
    //hi


    private ImageButton workout_IMBTN_exercise;
    private ImageButton workout_mIMBTN_practiceAlon;
    private Button workout_BTN_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        findView();

        workout_IMBTN_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutActivity.this, ShowPracticeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        workout_mIMBTN_practiceAlon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutActivity.this, PracticeInstructionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        workout_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    private void findView() {
        workout_BTN_back = findViewById(R.id.workout_BTN_back);
        workout_IMBTN_exercise = findViewById(R.id.workout_IMBTN_exercise);
        workout_mIMBTN_practiceAlon = findViewById(R.id.workout_mIMBTN_practiceAlon);
    }
}
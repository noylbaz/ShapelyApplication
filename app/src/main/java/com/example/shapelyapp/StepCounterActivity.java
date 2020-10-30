//package com.example.shapelyapp;
//
//import android.content.Intent;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;

//public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
//    private Button stepCounter_BTN_CounterReset;
//    private TextView stepCounter_TXT_showsteps ;
//    private TextView stepCounter_TXT_counter;
//    private Button stepCouner_BTN_back;
//    private SensorManager sensorManager;
//    private Sensor stepSensor;
//    private boolean isCounter;
//    int spetCount=0;

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_step_counter);
//
//        findView();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
//            stepSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//            isCounter=true;
//        }
//        else{
//            stepCounter_TXT_showsteps.setText("Counter is not present");
//            isCounter=false;
//        }
//
//        stepCounter_BTN_CounterReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stepSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER - (Sensor.TYPE_STEP_COUNTER));
//                spetCount=0;
//                stepCounter_TXT_counter.setText(String.valueOf(spetCount));
//            }
//        });
//        stepCouner_BTN_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(StepCounterActivity.this, MenuActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//
//    private void findView() {
//        stepCounter_TXT_showsteps = findViewById(R.id.stepCounter_TXT_showsteps);
//        stepCounter_TXT_counter = findViewById(R.id.stepCounter_TXT_counter);
//        stepCounter_BTN_CounterReset= findViewById(R.id.stepCounter_BTN_CounterReset);
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        stepCouner_BTN_back = findViewById(R.id.stepCouner_BTN_back);
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (sensorEvent.sensor == stepSensor){
//            spetCount= (int)sensorEvent.values[0];
//            stepCounter_TXT_counter.setText(String.valueOf(spetCount));
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//    }
//    @Override
//    protected void onResume(){
//        super.onResume();
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
//            sensorManager.registerListener(this,stepSensor ,SensorManager.SENSOR_DELAY_NORMAL);
//        }
//
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
//            sensorManager.unregisterListener(this,stepSensor );
//        }
//    }

//}
package com.example.shapelyapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    private static final int PHYSICAL_ACTIVITY = 1;
    private TextView tv_2;
    private SensorManager sensorManager;
    private static Boolean running = true;
    private static float delta;
    private static float lastKnownValue;
    private final static String SHARED_PREF = "sharedPrefs";
    private final static String LAST_SEN_VAL = "value";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        tv_2 = findViewById(R.id.stepCounter_TXT_counter);
        Button resetButton = findViewById(R.id.stepCounter_BTN_CounterReset);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PHYSICAL_ACTIVITY);
        }
        delta = loadData();
        initCounter();
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                initCounter();
                delta = loadData();
                running = true;
            }
        });
    }

    private void saveData(float value) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(LAST_SEN_VAL, value);
        editor.apply();

    }

    private void initCounter() {
        tv_2.setText("0.0");
    }

    private float loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        lastKnownValue = sharedPreferences.getFloat(LAST_SEN_VAL, 0);
        return lastKnownValue;
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
        loadData();
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
        saveData(lastKnownValue);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            tv_2.setText(String.valueOf(event.values[0] - delta));
            saveData(event.values[0]);
            Log.i("TAG", "onSensorChanged: " + event.values[0]);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
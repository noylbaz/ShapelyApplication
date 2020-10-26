package com.example.shapelyapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
    private Button stepCounter_BTN_CounterReset;
    private TextView stepCounter_TXT_showsteps ;
    private TextView stepCounter_TXT_counter;
    private Button stepCouner_BTN_back;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private boolean isCounter;
    int spetCount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        findView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            stepSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounter=true;
        }
        else{
            stepCounter_TXT_showsteps.setText("Counter is not present");
            isCounter=false;
        }

        stepCounter_BTN_CounterReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                spetCount=0;
                stepCounter_TXT_counter.setText(String.valueOf(spetCount));
            }
        });
        stepCouner_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StepCounterActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void findView() {
        stepCounter_TXT_showsteps = findViewById(R.id.stepCounter_TXT_showsteps);
        stepCounter_TXT_counter = findViewById(R.id.stepCounter_TXT_counter);
        stepCounter_BTN_CounterReset= findViewById(R.id.stepCounter_BTN_CounterReset);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCouner_BTN_back = findViewById(R.id.stepCouner_BTN_back);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == stepSensor){
            spetCount= (int)sensorEvent.values[0];
            stepCounter_TXT_counter.setText(String.valueOf(spetCount));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.registerListener(this,stepSensor ,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.unregisterListener(this,stepSensor );
        }
    }
}

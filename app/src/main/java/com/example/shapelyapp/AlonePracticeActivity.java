package com.example.shapelyapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlonePracticeActivity extends AppCompatActivity {
    private int i = 1;
    Thread t = new Thread();
    private TextView practic_TXT_seconds;

    MediaPlayer mysound;
    private static final int HP_BAR = 100;
    private ProgressBar alone_PRB_timer;
    private final int DELAY_TIME = 5000;
    private ImageView alonpractic_IMV_ex;

    private Button alonpractic_BTM_exit;
    private Button alonpractic_BTM_startNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alone_practice);

        findView();

        timer(30, practic_TXT_seconds);
        Toast.makeText(AlonePracticeActivity.this, "finish", Toast.LENGTH_SHORT).show();

        alonpractic_BTM_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlonePracticeActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();

            }
        });
        alonpractic_BTM_startNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlonePracticeActivity.this, PracticeInstructionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        alone_PRB_timer = findViewById(R.id.alone_PRB_timer);
        alonpractic_IMV_ex = findViewById(R.id.alonpractic_IMV_ex);
        practic_TXT_seconds = findViewById(R.id.practic_TXT_seconds);
        alonpractic_BTM_exit = findViewById(R.id.alonpractic_BTM_exit);
        alonpractic_BTM_startNew = findViewById(R.id.alonpractic_BTM_startNew);

    }

    public void timer(int Seconds, final TextView tv) {
        new CountDownTimer(Seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int miliseconds = (int) (millisUntilFinished / 1000);
                int seconds = miliseconds % 60;
                tv.setText(String.format("%02d", seconds));
                alone_PRB_timer.setProgress((alone_PRB_timer.getProgress() - 1));
            }

            public void onFinish() {
                i++;
                if (i <= 12) {
                    showEx(i, alonpractic_IMV_ex);
                    updateBar();
                    try {
                        t.sleep(DELAY_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // delay();
                    timer(30, practic_TXT_seconds);
                } else {
                    finish();
                }
            }
        }.start();
    }

//    private void delay() {
//        final Handler handler = new Handler();
//        final int delay = DELAY_TIME;
//        handler.postDelayed(new Runnable() {
//            public void run() {
////                timer(5, practic_TXT_seconds);
//               Toast.makeText(AlonePracticeActivity.this, "delay", Toast.LENGTH_SHORT).show();
//                //  if (main_PRB_life_player1.getProgress() != 0 && main_PRB_life_player2.getProgress() != 0) {
//                handler.postDelayed(this, delay);
//            }
//        },delay);
//    }


    private void updateBar() {
        alone_PRB_timer.setProgress(HP_BAR);
    }

    private void showEx(int numOfEx, ImageView imgPractic) {
        switch (numOfEx) {
            case 1:
                imgPractic.setImageResource(R.drawable.ic_ex1);
                Toast.makeText(AlonePracticeActivity.this, "show1", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                imgPractic.setImageResource(R.drawable.ic_ex2);
                Toast.makeText(AlonePracticeActivity.this, "show2", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                imgPractic.setImageResource(R.drawable.ic_ex3);
                Toast.makeText(AlonePracticeActivity.this, "show3", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                imgPractic.setImageResource(R.drawable.ic_ex4);
                Toast.makeText(AlonePracticeActivity.this, "show4", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                imgPractic.setImageResource(R.drawable.ic_ex5);
                Toast.makeText(AlonePracticeActivity.this, "show5", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                imgPractic.setImageResource(R.drawable.ic_ex6);
                Toast.makeText(AlonePracticeActivity.this, "show6", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                imgPractic.setImageResource(R.drawable.ic_ex7);
                Toast.makeText(AlonePracticeActivity.this, "show7", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                imgPractic.setImageResource(R.drawable.ic_ex8);
                Toast.makeText(AlonePracticeActivity.this, "show8", Toast.LENGTH_SHORT).show();
                break;
            case 9:
                imgPractic.setImageResource(R.drawable.ic_ex9);
                Toast.makeText(AlonePracticeActivity.this, "show9", Toast.LENGTH_SHORT).show();
                break;
            case 10:
                imgPractic.setImageResource(R.drawable.ic_ex10);
                Toast.makeText(AlonePracticeActivity.this, "show10", Toast.LENGTH_SHORT).show();
                break;
            case 11:
                imgPractic.setImageResource(R.drawable.ic_ex11);
                Toast.makeText(AlonePracticeActivity.this, "show11", Toast.LENGTH_SHORT).show();
                break;
            case 12:
                imgPractic.setImageResource(R.drawable.ic_ex12);
                Toast.makeText(AlonePracticeActivity.this, "show12", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
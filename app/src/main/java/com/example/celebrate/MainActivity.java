package com.example.celebrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //timer for onScreen animation to jump into main task
        CountDownTimer cdt = new CountDownTimer(1500,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                //getting into main application of app
                startActivity(new Intent(MainActivity.this,MainActivity2Main.class));
            }
        };
        cdt.start();
    }
}
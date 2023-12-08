package com.example.empty_parkinglot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 클릭 리스너 연동
        findViewById(R.id.parkinfoBtn).setOnClickListener(this);
        findViewById(R.id.reservBtn).setOnClickListener(this);
        findViewById(R.id.userjoinBtn).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parkinfoBtn:
                // 주차장 정보

                break;

            case R.id.reservBtn:
                startActivity(new Intent(this, ReservationActivity.class));
                break;

            case R.id.userjoinBtn:
                startActivity(new Intent(this, UserActivity.class));
                break;
        }
    }
}
package com.example.empty_parkinglot;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empty_parkinglot.database.MyRoomDatabase;
import com.example.empty_parkinglot.database.UserEntity;

/**
 * 개인정보 수정 (유저정보 화면)
 */
public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private MyRoomDatabase myRoomDatabase;           // 룸 데이터베이스
    private ParkingLotSingleton parkingLotSingleton; // 현재 로그인 된 유저 정보를 가지고 있는 싱글톤 디자인 패턴의 클래스
    private EditText mEtPassword, mEtPhone, mEtCarNum;
    private TextView mTvId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // init room database
        myRoomDatabase = MyRoomDatabase.getInstance(getApplicationContext());

        // init singleton class
        parkingLotSingleton = ParkingLotSingleton.getInstance();

        TextView tvName = findViewById(R.id.nameText);
        TextView tvTotalPrice = findViewById(R.id.tv_total_price);
        TextView tvParkingPosition = findViewById(R.id.tv_parking_position);

        mTvId = findViewById(R.id.idText);
        mEtPassword = findViewById(R.id.pwText);
        mEtPhone = findViewById(R.id.telText);
        mEtCarNum = findViewById(R.id.carnumText);

        // 저장 값 가져오기
        mTvId.setText(parkingLotSingleton.getMyUserId());
        mEtPassword.setText(parkingLotSingleton.getMyUserPw());
        mEtPhone.setText(parkingLotSingleton.getMyUserPhone());
        mEtCarNum.setText(parkingLotSingleton.getMyUserCarNum());
        tvName.setText(parkingLotSingleton.getMyUserName());

        findViewById(R.id.registerdoneBtn).setOnClickListener(this);
        findViewById(R.id.logoutBtn).setOnClickListener(this);

        // 데이터베이스의 로그인 정보를 가지고와서 주차 자리, 총 가격을 가져와서 보여준다
        new Thread(() -> {
            UserEntity userEntity = myRoomDatabase.userDao().getSelectedReadData(parkingLotSingleton.getMyUserId());
            runOnUiThread(() -> {
                tvParkingPosition.setText("차량 주차 자리 : " + userEntity.getUserParkingNum() + "번");
                tvTotalPrice.setText("결제 누적 금액 : " + userEntity.getUserTotalPay() + "원");
            });
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerdoneBtn:
                // 수정하기
                /* 유효성 검사 */
                if (TextUtils.isEmpty(mEtPassword.getText().toString()) ||
                        TextUtils.isEmpty(mEtPhone.getText().toString()) ||
                        TextUtils.isEmpty(mEtCarNum.getText().toString())) {
                    Toast.makeText(this, "비어있는 입력 값이 존재 합니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    UserEntity userEntity = myRoomDatabase.userDao().getSelectedReadData(parkingLotSingleton.myUserId);
                    userEntity.setUserPw(mEtPassword.getText().toString());
                    userEntity.setUserCarNum(mEtCarNum.getText().toString());
                    userEntity.setUserPhone(mEtPhone.getText().toString());

                    myRoomDatabase.userDao().updateData(userEntity);

                    runOnUiThread(() -> Toast.makeText(UserActivity.this, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show());
                    finish();
                }).start();
                break;

            case R.id.logoutBtn:
                // 로그아웃 버튼

                parkingLotSingleton = null;
                Toast.makeText(this, "로그아웃이 완료되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                finishAffinity();
                break;

        }
    }
}

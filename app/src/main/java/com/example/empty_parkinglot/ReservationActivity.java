package com.example.empty_parkinglot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.empty_parkinglot.database.MyRoomDatabase;
import com.example.empty_parkinglot.database.UserEntity;

import java.util.ArrayList;

/**
 * 주차장 예약 화면
 */
public class ReservationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvCurrentPos;                  // 주차 (예약) 한 자리
    private MyRoomDatabase myRoomDatabase;           // 룸 데이터베이스
    private ParkingLotSingleton parkingLotSingleton; // 현재 로그인 된 유저 정보를 가지고 있는 싱글톤 디자인 패턴의 클래스
    private Button
            mBtnPosition1,
            mBtnPosition2,
            mBtnPosition3,
            mBtnPosition4,
            mBtnPosition5,
            mBtnPosition6,
            mBtnPosition7,
            mBtnPosition8,
            mBtnPosition9,
            mBtnPosition10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // init room database
        myRoomDatabase = MyRoomDatabase.getInstance(getApplicationContext());

        // init singleton class
        parkingLotSingleton = ParkingLotSingleton.getInstance();

        mBtnPosition1 = findViewById(R.id.btn_position_1);
        mBtnPosition2 = findViewById(R.id.btn_position_2);
        mBtnPosition3 = findViewById(R.id.btn_position_3);
        mBtnPosition4 = findViewById(R.id.btn_position_4);
        mBtnPosition5 = findViewById(R.id.btn_position_5);
        mBtnPosition6 = findViewById(R.id.btn_position_6);
        mBtnPosition7 = findViewById(R.id.btn_position_7);
        mBtnPosition8 = findViewById(R.id.btn_position_8);
        mBtnPosition9 = findViewById(R.id.btn_position_9);
        mBtnPosition10 = findViewById(R.id.btn_position_10);

        mBtnPosition1.setOnClickListener(this);
        mBtnPosition2.setOnClickListener(this);
        mBtnPosition3.setOnClickListener(this);
        mBtnPosition4.setOnClickListener(this);
        mBtnPosition5.setOnClickListener(this);
        mBtnPosition6.setOnClickListener(this);
        mBtnPosition7.setOnClickListener(this);
        mBtnPosition8.setOnClickListener(this);
        mBtnPosition9.setOnClickListener(this);
        mBtnPosition10.setOnClickListener(this);

        mTvCurrentPos = findViewById(R.id.tv_parking_position);

        new Thread(() -> {
            UserEntity userEntity = myRoomDatabase.userDao().getSelectedReadData(parkingLotSingleton.getMyUserId());
            runOnUiThread(() -> {
                if (userEntity.getUserParkingNum() == 0)
                    mTvCurrentPos.setText("-");
                else
                    mTvCurrentPos.setText(String.valueOf(userEntity.getUserParkingNum()));
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRecentReservationInfo();
    }

    private void getRecentReservationInfo() {
        // 최근 사용자들의 예약정보들을 모두 가져와서 내가 예약 가능한자리와 불가능한 자리를 표시한다
        new Thread(() -> {
            ArrayList<UserEntity> lstUser = (ArrayList<UserEntity>) myRoomDatabase.userDao().getAllData();
            if (lstUser != null && !lstUser.isEmpty()) {
                for (int i = 0; i < lstUser.size(); i++) {
                    int parkingPos = lstUser.get(i).getUserParkingNum();
                    runOnUiThread(() -> {
                        switch (parkingPos) {
                            case 0:

                                break;

                            case 1:
                                mBtnPosition1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 2:
                                mBtnPosition2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 3:
                                mBtnPosition3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 4:
                                mBtnPosition4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 5:
                                mBtnPosition5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 6:
                                mBtnPosition6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 7:
                                mBtnPosition7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 8:
                                mBtnPosition8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 9:
                                mBtnPosition9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;

                            case 10:
                                mBtnPosition10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                break;
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_position_1:
                showReservationDialog(view, 1);
                break;

            case R.id.btn_position_2:
                showReservationDialog(view,2);
                break;

            case R.id.btn_position_3:
                showReservationDialog(view,3);
                break;

            case R.id.btn_position_4:
                showReservationDialog(view,4);
                break;

            case R.id.btn_position_5:
                showReservationDialog(view,5);
                break;

            case R.id.btn_position_6:
                showReservationDialog(view,6);
                break;

            case R.id.btn_position_7:
                showReservationDialog(view,7);
                break;

            case R.id.btn_position_8:
                showReservationDialog(view,8);
                break;

            case R.id.btn_position_9:
                showReservationDialog(view,9);
                break;

            case R.id.btn_position_10:
                showReservationDialog(view,10);
                break;
        }
    }

    private void showReservationDialog(View _clickView, int _parkingPosition) {
        new AlertDialog.Builder(this)
                .setTitle("이용 안내")
                .setMessage(_parkingPosition + "번" + " 자리\n열정 주차장에 예약하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(() -> {
                            if (_clickView.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#9E9E9E"))) {
                                runOnUiThread(() -> Toast.makeText(ReservationActivity.this, "예약 불가한 주차번호 입니다", Toast.LENGTH_SHORT).show());
                                return;
                            }

                            // 로그인 된 아이디에 대한 유저정보 db select (조회)
                            UserEntity userEntity = myRoomDatabase.userDao().getSelectedReadData(parkingLotSingleton.getMyUserId());
                            // 주차 예약 번호 삽입
                            userEntity.setUserParkingNum(_parkingPosition);
                            // db update (parking position)
                            myRoomDatabase.userDao().updateData(userEntity);
                            // 비동기 스레드 내부에서 UI 제어하는 처리는 ui thread로 수행시켜줘야 에러를 방지할 수 있다
                            runOnUiThread(() -> {
                                _clickView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                                mTvCurrentPos.setText(String.valueOf(_parkingPosition));
                                Toast.makeText(ReservationActivity.this, "예약 완료 되었습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ReservationActivity.this, PaymentActivity.class);
                                intent.putExtra("parking_pos", _parkingPosition);
                                startActivityForResult(intent, 777);
                            });
                        }).start();
                    }
                })
                .setNegativeButton("차량퇴차", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 로그인 된 아이디에 대한 유저정보 db select (조회)
                                UserEntity userEntity = myRoomDatabase.userDao().getSelectedReadData(parkingLotSingleton.getMyUserId());
                                // 내가 예약했던 주차번호 였는지 체크해서 일치하는 경우에만 퇴차 가능
                                if (userEntity.getUserParkingNum() != _parkingPosition) {
                                    runOnUiThread(() -> Toast.makeText(ReservationActivity.this, "본인이 예약했던 주차번호만 퇴차가 가능합니다", Toast.LENGTH_SHORT).show());
                                    return;
                                }

                                // 퇴차 처리를 위한 0으로 초기화
                                userEntity.setUserParkingNum(0);
                                // db update (parking position)
                                myRoomDatabase.userDao().updateData(userEntity);
                                // 비동기 스레드 내부에서 UI 제어하는 처리는 ui thread로 수행시켜줘야 에러를 방지할 수 있다
                                runOnUiThread(() -> {
                                    mTvCurrentPos.setText("-");
                                    _clickView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    Toast.makeText(ReservationActivity.this, "퇴차 처리 되었습니다", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }).start();
                    }
                })
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            if (resultCode == RESULT_OK) {
                // 결제 화면에서 결제 처리를 정상적으로 완료 하고 돌아오면 이 곳을 수행한다
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                int parkingPos;
                if (data != null) {
                    parkingPos = data.getIntExtra("parking_pos", -1);

                    // 결제 취소 버튼 누르거나, 백버튼 눌러서 돌아온 상황
                    int finalParkingPos = parkingPos;
                    new Thread(() -> {
                        // 결제가 완료된 것이 아니기에 예약도 취소 시켜야함!
                        UserEntity userEntity = myRoomDatabase.userDao().getSelectedReadData(parkingLotSingleton.myUserId);
                        userEntity.setUserParkingNum(0);
                        myRoomDatabase.userDao().updateData(userEntity);
                        runOnUiThread(() -> {
                            mTvCurrentPos.setText("-");

                            switch (finalParkingPos) {
                                case 1:
                                    mBtnPosition1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 2:
                                    mBtnPosition2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 3:
                                    mBtnPosition3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 4:
                                    mBtnPosition4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 5:
                                    mBtnPosition5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 6:
                                    mBtnPosition6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 7:
                                    mBtnPosition7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 8:
                                    mBtnPosition8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 9:
                                    mBtnPosition9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;

                                case 10:
                                    mBtnPosition10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#536DFE")));
                                    break;
                            }
                        });
                    }).start();
                }
            }
        }
    }
}
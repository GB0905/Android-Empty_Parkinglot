package com.example.empty_parkinglot;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empty_parkinglot.database.MyRoomDatabase;
import com.example.empty_parkinglot.database.UserEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 결제 화면
 */
public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvStartParking, mTvEndParking, mTvUsingTime, mTvUsingPrice;
    private MyRoomDatabase myRoomDatabase;           // 룸 데이터베이스
    private ParkingLotSingleton parkingLotSingleton; // 현재 로그인 된 유저 정보를 가지고 있는 싱글톤 디자인 패턴의 클래스

    private int current_parking_pos;                 // 예약 화면에서 정해졌던 주차번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            current_parking_pos = intent.getIntExtra("parking_pos", -1);
        }

        // init room database
        myRoomDatabase = MyRoomDatabase.getInstance(getApplicationContext());

        // init singleton class
        parkingLotSingleton = ParkingLotSingleton.getInstance();

        mTvStartParking = findViewById(R.id.btn_start_parking_time);
        mTvEndParking = findViewById(R.id.btn_end_parking_time);
        mTvUsingTime = findViewById(R.id.tv_using_time);
        mTvUsingPrice = findViewById(R.id.tv_using_price);

        mTvStartParking.setOnClickListener(this);
        mTvEndParking.setOnClickListener(this);
        findViewById(R.id.btn_payment).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_parking_time:
                // 주차 시작 시간 설정
                showDateAndTimeDialog("start");
                break;

            case R.id.btn_end_parking_time:
                // 주차 종료 시간 설정
                showDateAndTimeDialog("end");
                break;

            case R.id.btn_payment:
                // 결제하기
                String startParkingTime = mTvStartParking.getText().toString();
                String endParkingTime = mTvEndParking.getText().toString();
                if (startParkingTime.equals("날짜/시간") || endParkingTime.equals("날짜/시간")) {
                    Toast.makeText(this, "먼저, 날짜와 시간을 설정해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    UserEntity userEntity = myRoomDatabase.userDao().getSelectedReadData(parkingLotSingleton.getMyUserId());
                    userEntity.setUserTotalPay(userEntity.getUserTotalPay() + Integer.parseInt(mTvUsingPrice.getText().toString().replace("원","")));
                    // 결제 정보 누적 방식으로 update
                    myRoomDatabase.userDao().updateData(userEntity);

                    runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "결제가 완료되었습니다", Toast.LENGTH_SHORT).show());
                    setResult(RESULT_OK);
                    finish();
                }).start();
                break;

            case R.id.btn_cancel:
                // 결제 취소
                Intent intent = new Intent();
                intent.putExtra("parking_pos", current_parking_pos);
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }
    }

    private void showDateAndTimeDialog(String _type) {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                sb.append(year).append("/").append(String.format(Locale.getDefault(), "%02d",monthOfYear + 1)).append("/").append(String.format(Locale.getDefault(), "%02d",dayOfMonth));
                TimePickerDialog timePickerDialog = new TimePickerDialog(PaymentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        sb.append(" ").append(String.format(Locale.getDefault(), "%02d",hourOfDay)).append(":").append(String.format(Locale.getDefault(), "%02d",minute));
                        if (_type.equals("start")) {
                            mTvStartParking.setText(sb.toString());
                        } else {
                            mTvEndParking.setText(sb.toString());
                        }

                        String startParkingTime = mTvStartParking.getText().toString();
                        String endParkingTime = mTvEndParking.getText().toString();

                        if (!startParkingTime.equals("날짜/시간") && !endParkingTime.equals("날짜/시간")) {
                            try {
                                Date startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(startParkingTime);
                                Date endDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(endParkingTime);

                                // 시간 차 계산 (종료시간 - 주차시작)
                                long diffTime = endDate.getTime() - startDate.getTime();
                                long minutes = (diffTime / 1000)  / 60;


                                // 만약에 음수라면 시간 설정 다시 하게 해야함
                                if (minutes < 0) {
                                    mTvStartParking.setText("날짜/시간");
                                    mTvEndParking.setText("날짜/시간");
                                    Toast.makeText(PaymentActivity.this, "종료시간이 주차시작 시간보다 빠르게 설정할 수 없습니다", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // 이용 시간 계산
                                mTvUsingTime.setText(minutes + "분");
                                // 요금 1분 당 100원 으로 계산
                                mTvUsingPrice.setText((minutes * 100) + "원");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, 12, 0, false);
                timePickerDialog.setTitle("시간을 설정해주세요");
                timePickerDialog.show();

            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("날짜를 설정해주세요");
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        // 백버튼을 눌렀을 때 처리
        Intent intent = new Intent();
        intent.putExtra("parking_pos", current_parking_pos);
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }
}
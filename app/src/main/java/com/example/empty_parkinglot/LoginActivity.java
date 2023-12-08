package com.example.empty_parkinglot;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empty_parkinglot.database.MyRoomDatabase;
import com.example.empty_parkinglot.database.UserEntity;

import java.util.ArrayList;

/**
 * 로그인 화면
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtId, mEtPw;
    private MyRoomDatabase myRoomDatabase;
    private ParkingLotSingleton parkingLotSingleton; // 현재 로그인 된 유저 정보를 가지고 있는 싱글톤 디자인 패턴의 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // room data base init
        myRoomDatabase = MyRoomDatabase.getInstance(getApplicationContext());

        // init singleton class
        parkingLotSingleton = ParkingLotSingleton.getInstance();

        // 입력필드 뷰
        mEtId = findViewById(R.id.idText);
        mEtPw = findViewById(R.id.pwText);

        // 클릭 리스너 연동
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.registerBtn).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                // 로그인

                /* 유효성 검사 */
                if (TextUtils.isEmpty(mEtId.getText().toString()) || TextUtils.isEmpty(mEtPw.getText().toString())) {
                    Toast.makeText(this, "비어있는 입력 값이 있습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 룸 데이터베이스에 접근하려면 비동기 스레드 환경이어야 하므로 별도 스레드 생성
                new Thread(() -> {
                    boolean isLoginSuccess = false;

                    // 데이터베이스에서 유저정보들을 모두 가져온다
                    ArrayList<UserEntity> lstUser = (ArrayList<UserEntity>) myRoomDatabase.userDao().getAllData();
                    // 반복문을 돌려서 내가 로그인 시도한 값과 비교한다
                    for (int i = 0; i < lstUser.size(); i++) {
                        if (lstUser.get(i).getUserId().equals(mEtId.getText().toString()) && lstUser.get(i).getUserPw().equals(mEtPw.getText().toString())) {
                            UserEntity userEntity = lstUser.get(i);
                            // 여러 화면에서의 활용을 위해 현재 로그인 성공된 정보를 싱글톤 클래스에 임시 저장한다
                            parkingLotSingleton.setMyUserId(userEntity.getUserId());
                            parkingLotSingleton.setMyUserPw(userEntity.getUserPw());
                            parkingLotSingleton.setMyUserName(userEntity.getUserName());
                            parkingLotSingleton.setMyUserPhone(userEntity.getUserPhone());
                            parkingLotSingleton.setMyUserCarNum(userEntity.getUserCarNum());

                            isLoginSuccess = true; // 아이디, 비밀번호 일치
                            break;
                        }
                    }

                    // 로그인 실패
                    if (!isLoginSuccess) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    // 로그인 성공 & 메인 이동
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "환영합니다!", Toast.LENGTH_SHORT).show());
                    finish();
                }).start();


                break;

            case R.id.registerBtn:
                // 회원가입
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }
}
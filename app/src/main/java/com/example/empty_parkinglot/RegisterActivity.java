package com.example.empty_parkinglot;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empty_parkinglot.database.MyRoomDatabase;
import com.example.empty_parkinglot.database.UserEntity;

/**
 * 회원 가입 화면
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtId, mEtPassword, mEtPasswordRe, mEtName, mEtPhone, mEtCarNum;
    private Boolean isDuplicated = null; // 중복 아이디 여부
    private MyRoomDatabase myRoomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // room database init
        myRoomDatabase = MyRoomDatabase.getInstance(getApplicationContext());

        // 입력 필드
        mEtId = findViewById(R.id.idText);
        mEtPassword = findViewById(R.id.pwText);
        mEtPasswordRe = findViewById(R.id.pwcheckText);
        mEtName = findViewById(R.id.nameText);
        mEtPhone = findViewById(R.id.telText);
        mEtCarNum = findViewById(R.id.carnumText);

        // 클릭 리스너 연동
        findViewById(R.id.idcheckBtn).setOnClickListener(this);
        findViewById(R.id.registerdoneBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idcheckBtn:
                // 중복 확인
                if (TextUtils.isEmpty(mEtId.getText().toString())) {
                    Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    // db 에 회원가입 시도하는 아이디 값이 존재하는지 체크
                    if (myRoomDatabase.userDao().getSelectedReadData(mEtId.getText().toString()) != null)
                        isDuplicated = true;
                    else
                        isDuplicated = false;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isDuplicated) {
                                // 중복 확인을 했으나, 중복 된 결과 값
                                Toast.makeText(RegisterActivity.this, "중복 된 아이디가 있습니다", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "사용 가능한 아이디 입니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }).start();
                break;

            case R.id.registerdoneBtn:
                // 가입 완료

                /* 유효성 검사 */
                if (TextUtils.isEmpty(mEtId.getText().toString()) ||
                        TextUtils.isEmpty(mEtPassword.getText().toString()) ||
                        TextUtils.isEmpty(mEtPasswordRe.getText().toString()) ||
                        TextUtils.isEmpty(mEtName.getText().toString()) ||
                        TextUtils.isEmpty(mEtPhone.getText().toString()) ||
                        TextUtils.isEmpty(mEtCarNum.getText().toString())) {
                    Toast.makeText(this, "비어있는 입력 값이 존재 합니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isDuplicated == null || isDuplicated) {
                    Toast.makeText(this, "중복체크가 완료되어야 합니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!mEtPassword.getText().toString().equals(mEtPasswordRe.getText().toString())) {
                    Toast.makeText(this, "비밀번호 입력 값이 서로 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 새 사용자 정보 생성
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(mEtId.getText().toString());
                userEntity.setUserPw(mEtPassword.getText().toString());
                userEntity.setUserName(mEtName.getText().toString());
                userEntity.setUserPhone(mEtPhone.getText().toString());
                userEntity.setUserCarNum(mEtCarNum.getText().toString());
                
                new Thread(() -> {
                    // db insert user account
                    myRoomDatabase.userDao().insertData(userEntity);
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "회원가입이 완료 되었습니다", Toast.LENGTH_SHORT).show());
                    finish();
                }).start();
                break;
        }
    }
}
package com.example.empty_parkinglot.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 데이터베이스 모델 객체
 */
@Entity(tableName = "tb_user")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "user_id")
    private String userId = "";        // 사용자 아이디

    @ColumnInfo(name = "user_pw")
    private String userPw = "";        // 사용자 비밀번호

    @ColumnInfo(name = "user_name")
    private String userName = "";      // 사용자 이름

    @ColumnInfo(name = "user_phone")
    private String userPhone = "";     // 사용자 휴대폰 번호

    @ColumnInfo(name = "user_car_num")
    private String userCarNum = "";     // 자동차 등록 번호

    @ColumnInfo(name = "user_parking_num")
    private int userParkingNum = 0;     // 차량 주차 자리 (번호)

    @ColumnInfo(name = "user_total_pay")
    private int userTotalPay = 0;     // 차량 주차 자리 (번호)


    /* getter & setter */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserCarNum() {
        return userCarNum;
    }

    public void setUserCarNum(String userCarNum) {
        this.userCarNum = userCarNum;
    }

    public int getUserParkingNum() {
        return userParkingNum;
    }

    public void setUserParkingNum(int userParkingNum) {
        this.userParkingNum = userParkingNum;
    }

    public int getUserTotalPay() {
        return userTotalPay;
    }

    public void setUserTotalPay(int userTotalPay) {
        this.userTotalPay = userTotalPay;
    }
}

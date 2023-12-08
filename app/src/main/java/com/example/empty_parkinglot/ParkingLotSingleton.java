package com.example.empty_parkinglot;

/**
 * 싱글톤 디자인 패턴 클래스 (로그인 된 유저 정보를 임시적으로 저장해두기 위해 쓴다)
 */
public class ParkingLotSingleton {
    private static ParkingLotSingleton instance = null;
    public String myUserId;     // 현재 로그인 된 내 아이디
    public String myUserPw;     // 현재 로그인 된 내 비밀번호
    public String myUserName;   // 현재 로그인 된 내 이름
    public String myUserPhone;  // 현재 로그인 된 내 폰번호
    public String myUserCarNum; // 현재 로그인 된 내 차량 번호판

    public static ParkingLotSingleton getInstance() {
        if (instance == null)
            instance = new ParkingLotSingleton();
        return instance;
    }


    /* getter & setter */
    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    public String getMyUserPw() {
        return myUserPw;
    }

    public void setMyUserPw(String myUserPw) {
        this.myUserPw = myUserPw;
    }

    public String getMyUserName() {
        return myUserName;
    }

    public void setMyUserName(String myUserName) {
        this.myUserName = myUserName;
    }

    public String getMyUserPhone() {
        return myUserPhone;
    }

    public void setMyUserPhone(String myUserPhone) {
        this.myUserPhone = myUserPhone;
    }

    public String getMyUserCarNum() {
        return myUserCarNum;
    }

    public void setMyUserCarNum(String myUserCarNum) {
        this.myUserCarNum = myUserCarNum;
    }
}

package com.example.empty_parkinglot.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * 내부 데이터베이스 쿼리 요청을 할 수 있는
 * DAO (Data Access Object)
 */
@Dao
public interface UserDao {
    // database table 에 삽입 (추가)
    @Insert
    void insertData(UserEntity entity);

    // database table 에 기존에 존재하는 데이터를 수정
    @Update
    void updateData(UserEntity entity);

    // database table 에 기존에 존재하는 데이터를 삭제
    @Delete
    void deleteData(UserEntity entity);

    // database table 에 모든 데이터를 삭제 한다.
    @Query("DELETE FROM tb_user")
    void deleteAllData();

    // 사용자 정보 모든 데이터를 가지고옴
    @Query("SELECT * FROM tb_user")
    List<UserEntity> getAllData();

    // 유효성 체크 (아이디 중복 검사 시)
    @Query("SELECT * FROM tb_user WHERE user_id = :userId")
    UserEntity getSelectedReadData(String userId);
}

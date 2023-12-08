package com.example.empty_parkinglot.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kotlin.jvm.Synchronized;

/**
 * Room Database 를 싱글톤 패턴 형식으로 구현하여
 * 어떤 곳에서 가져다 쓰기에도 편리하게 구성함.
 */
@Database(entities = UserEntity.class, version = 1)
public abstract class MyRoomDatabase extends RoomDatabase {
    public static MyRoomDatabase instance = null;

    public abstract UserDao userDao();

    @Synchronized
    public static MyRoomDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MyRoomDatabase.class) {
                instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        MyRoomDatabase.class,
                        "parkinglot_database"
                ).build();
            }
        }
        return instance;
    }
}

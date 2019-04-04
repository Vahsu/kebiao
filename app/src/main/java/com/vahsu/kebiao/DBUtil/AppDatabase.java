package com.vahsu.kebiao.DBUtil;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CourseEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CourseDao CourseDao();

    private static AppDatabase sInstanse;

    public static AppDatabase getInstance(Context context) {
        if (sInstanse == null) {
            sInstanse = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "course.db").build();
        }
        return sInstanse;
    }

    public static void onDestroy() {
        sInstanse = null;
    }
}

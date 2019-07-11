package com.vahsu.kebiao.DBUtil;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CourseDao {


    //通过周数查找课程信息
    @Query("SELECT length, courseName, classroom, type, lecturer FROM course WHERE week = :week ORDER BY num")
    List<CourseLNRTL> getCourseByWeek(int week);

    //通过周数查找一周的日期
    @Query("SELECT DISTINCT date FROM course WHERE week = :week ORDER BY num")
    List<String> getDatesByWeek(int week);

    //通过日期查找周数
    @Query("SELECT week FROM course WHERE date = :date")
    int getWeekByDate(String date);

    //通过List插入全部course记录
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAll (List<CourseEntity> courseEntities);

    //删除全部课表信息
    @Query("delete FROM course")
    void deleteAll();

}

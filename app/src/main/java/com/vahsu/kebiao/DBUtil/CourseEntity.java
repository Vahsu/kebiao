package com.vahsu.kebiao.DBUtil;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course")
public class CourseEntity {

    @PrimaryKey
    private int num;

    private int length;
    private String courseName;
    private String classroom;
    private String type;
    private String lecturer;
    private int week;
    private int day;
    private String date;


    public CourseEntity(int num, int length, String courseName, String classroom,
                        String type, String lecturer, int week, int day, String date) {

        this.num = num;
        this.length = length;
        this.courseName = courseName;
        this.classroom = classroom;
        this.type = type;
        this.lecturer = lecturer;
        this.week = week;
        this.day = day;
        this.date = date;

    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }
}

package com.vahsu.kebiao.DBUtil;

import androidx.room.ColumnInfo;

public class CourseLNR {
    @ColumnInfo(name = "length")
    private int length;

    @ColumnInfo(name = "courseName")
    private String courseName;

    @ColumnInfo(name = "classroom")
    private String classroom;

    public int getLength() {
        return length;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}

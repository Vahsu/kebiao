package com.vahsu.kebiao.DBUtil;

import androidx.room.ColumnInfo;

public class CourseLNRTL {
    @ColumnInfo(name = "length")
    private int length;

    @ColumnInfo(name = "courseName")
    private String courseName;

    @ColumnInfo(name = "classroom")
    private String classroom;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "lecturer")
    private String lecturer;

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

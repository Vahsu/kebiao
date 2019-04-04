package com.vahsu.kebiao.DBUtil;

import androidx.room.ColumnInfo;

public class CourseLNR {
    @ColumnInfo(name = "courseLength")
    private int courseLength;

    @ColumnInfo(name = "courseName")
    private String courseName;

    @ColumnInfo(name = "classroom")
    private String classroom;

    public int getCourseLength() {
        return courseLength;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setCourseLength(int courseLength) {
        this.courseLength = courseLength;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}

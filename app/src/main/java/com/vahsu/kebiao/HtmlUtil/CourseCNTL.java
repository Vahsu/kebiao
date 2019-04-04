package com.vahsu.kebiao.HtmlUtil;

public class CourseCNTL {
    private String courseCode;
    private String courseName;
    private String type;
    private String lecturer;

    CourseCNTL(String courseCode, String courseName, String type, String lecturer){
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.type = type;
        this.lecturer = lecturer;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getType() {
        return type;
    }

    public String getLecturer() {
        return lecturer;
    }
}

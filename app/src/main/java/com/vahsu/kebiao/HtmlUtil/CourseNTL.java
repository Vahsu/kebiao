package com.vahsu.kebiao.HtmlUtil;

public class CourseNTL {
    private String courseName;
    private String type;
    private String lecturer;

    CourseNTL(String courseName, String type, String lecturer){
        this.courseName = courseName;
        this.type = type;
        this.lecturer = lecturer;
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

package com.providentitgroup.attendergcuf.models;

import org.jsoup.select.Elements;

public class AttendanceItem {
    private String courseId;
    private String courseName;
    private String courseInstructor;
    private String numOfLectures;
    private String numOfPresents;
    private String numOfLeaves;
    private String numOfAbsents;
    private String attendancePercentage;

    public AttendanceItem(String courseId, String courseName, String courseInstructor, String numOfLectures, String numOfPresents, String numOfLeaves, String numOfAbsents, String attendancePercentage) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseInstructor = courseInstructor;
        this.numOfLectures = numOfLectures;
        this.numOfPresents = numOfPresents;
        this.numOfLeaves = numOfLeaves;
        this.numOfAbsents = numOfAbsents;
        this.attendancePercentage = attendancePercentage;
    }

    public AttendanceItem() {
    } 
    public AttendanceItem(Elements data) throws Exception {
        this.courseId = data.get(1).text();
        this.courseName = data.get(2).text();
        this.courseInstructor =  data.get(3).text();
        this.numOfLectures = data.get(4).text();
        this.numOfPresents = data.get(5).text();
        this.numOfLeaves = data.get(6).text();
        this.numOfAbsents = data.get(7).text();
        this.attendancePercentage = data.get(8).text();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public String getNumOfLectures() {
        return numOfLectures;
    }

    public void setNumOfLectures(String numOfLectures) {
        this.numOfLectures = numOfLectures;
    }

    public String getNumOfPresents() {
        return numOfPresents;
    }

    public void setNumOfPresents(String numOfPresents) {
        this.numOfPresents = numOfPresents;
    }

    public String getNumOfLeaves() {
        return numOfLeaves;
    }

    public void setNumOfLeaves(String numOfLeaves) {
        this.numOfLeaves = numOfLeaves;
    }

    public String getNumOfAbsents() {
        return numOfAbsents;
    }

    public void setNumOfAbsents(String numOfAbsents) {
        this.numOfAbsents = numOfAbsents;
    }

    public String getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(String attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
}

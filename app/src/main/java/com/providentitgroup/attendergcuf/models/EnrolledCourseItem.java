package com.providentitgroup.attendergcuf.models;

import com.providentitgroup.attendergcuf.Utility.DataValidator;

import org.jsoup.select.Elements;

public class EnrolledCourseItem {
    private String courseId;
    private String courseTitle;
    private String courseCreditHours;
    private String courseInstructor;

    public EnrolledCourseItem(String courseId, String courseTitle, String courseCreditHours, String courseInstructor) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseCreditHours = courseCreditHours;
        this.courseInstructor = courseInstructor;
    }

    public EnrolledCourseItem() {
    }
    public EnrolledCourseItem(Elements data) throws Exception {
        this.courseId = data.get(1).text().trim();
        this.courseTitle = data.get(2).text().trim();
        this.courseCreditHours = this.courseTitle.substring(this.courseTitle.length()-6).trim();
        this.courseTitle = DataValidator.toTitleCase(this.getCourseTitle().substring(0,this.courseTitle.length()-6).trim());
        this.courseInstructor = DataValidator.toTitleCase(data.get(3).text().trim());
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseCreditHours() {
        return courseCreditHours;
    }

    public void setCourseCreditHours(String courseCreditHours) {
        this.courseCreditHours = courseCreditHours;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }
}

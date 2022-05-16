package com.providentitgroup.attendergcuf.models;

import android.util.Log;

import com.providentitgroup.attendergcuf.Utility.DataValidator;

import org.jsoup.select.Elements;

public class QecCourseItem {
    private String courseId;
    private String courseTitle;
    private String courseCreditHours;
    private String courseInstructor;
    private String ratingLink;

    public QecCourseItem(String courseId, String courseTitle, String courseCreditHours, String courseInstructor, String ratingLink) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseCreditHours = courseCreditHours;
        this.courseInstructor = courseInstructor;
        this.ratingLink = ratingLink;
    }

    public QecCourseItem(Elements data) throws Exception{
        this.courseId = data.get(1).text().trim();
        this.courseTitle = data.get(2).text().trim();
        this.courseCreditHours = this.courseTitle.substring(this.courseTitle.length()-6).trim();
        this.courseTitle = DataValidator.toTitleCase(this.getCourseTitle().substring(0,this.courseTitle.length()-6).trim());
        this.courseInstructor = DataValidator.toTitleCase(data.get(3).text().trim());
        this.ratingLink = data.get(4).selectFirst(">a").attr("onclick");
        if(this.ratingLink.isEmpty() || this.ratingLink.matches("")){
            this.ratingLink=null;
        }else{
            this.ratingLink = this.getRatingLink().substring(13,this.ratingLink.length()-58);
            Log.d("TAG",this.ratingLink);
        }
    }

    public QecCourseItem() {
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

    public String getRatingLink() {
        return ratingLink;
    }

    public void setRatingLink(String ratingLink) {
        this.ratingLink = ratingLink;
    }
}

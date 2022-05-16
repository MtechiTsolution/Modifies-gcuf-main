package com.providentitgroup.attendergcuf.models;

import android.util.Log;

import org.jsoup.select.Elements;

public class MarkAttendanceItem {
    private String courseTitle;
    private String courseTiming;
    private String courseDay;
    private String courseSection;
    private String courseLink;

    public MarkAttendanceItem(String courseTitle, String courseTiming, String courseDay, String courseSection, String courseLink) {
        this.courseTitle = courseTitle;
        this.courseTiming = courseTiming;
        this.courseDay = courseDay;
        this.courseSection = courseSection;
        this.courseLink = courseLink;
    }

    public MarkAttendanceItem(Elements data) throws Exception {
        this.courseSection = data.get(1).text().trim();
        this.courseTitle=data.get(2).text().trim();
        this.courseDay=data.get(3).html().split("<br>")[0].trim();
        this.courseTiming=data.get(3).html().split("<br>")[1].trim();


        this.courseLink = data.get(7).selectFirst(">a").attr("onclick");
        if(this.courseLink.isEmpty() || this.courseLink.equals("#")){
            this.courseLink=null;
        }else{
            this.courseLink = this.getCourseLink().substring(10,this.getCourseLink().length()-2);
            Log.d("TAG",this.getCourseLink());
        }
    }
    public MarkAttendanceItem() {
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseTiming() {
        return courseTiming;
    }

    public void setCourseTiming(String courseTiming) {
        this.courseTiming = courseTiming;
    }

    public String getCourseDay() {
        return courseDay;
    }

    public void setCourseDay(String courseDay) {
        this.courseDay = courseDay;
    }

    public String getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(String courseSection) {
        this.courseSection = courseSection;
    }

    public String getCourseLink() {
        return courseLink;
    }

    public void setCourseLink(String courseLink) {
        this.courseLink = courseLink;
    }
}

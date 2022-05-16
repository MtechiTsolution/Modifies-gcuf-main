package com.providentitgroup.attendergcuf.models;

import android.util.Log;

import org.jsoup.select.Elements;

import java.util.ArrayList;

public class TeacherViewAttendanceItem {
    private String courseId;
    private String courseTitle;
    private String courseSection;
    private String courseCreditHours;
    private String numberOfLectures;
    private String numberOfStudents;
    private String detailsLink;

    public TeacherViewAttendanceItem(String courseId, String courseTitle, String courseSection, String courseCreditHours, String numberOfLectures, String numberOfStudents, String detailsLink) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseSection = courseSection;
        this.courseCreditHours = courseCreditHours;
        this.numberOfLectures = numberOfLectures;
        this.numberOfStudents = numberOfStudents;
        this.detailsLink = detailsLink;
    }
    public TeacherViewAttendanceItem(Elements data) throws Exception {
        String str = data.get(2).text().trim();
        this.courseId = str.split("-")[0].trim() + " - "+str.split("-")[1].trim();
        this.courseTitle = str.split("-")[2].trim();
        this.courseCreditHours = str.substring(str.length()-6);
        this.courseTitle= this.getCourseTitle().substring(0,this.getCourseTitle().length()-3).trim();
        this.courseSection = data.get(1).text().trim();
        this.numberOfLectures = data.get(4).text().trim();
        this.numberOfStudents = data.get(6).text().trim();
        this.detailsLink = data.get(8).selectFirst(">a").attr("onclick");
        if(this.detailsLink.isEmpty() || this.detailsLink.matches("")){
            this.detailsLink=null;
        }else{
            this.detailsLink = this.getDetailsLink().substring(10,this.getDetailsLink().length()-2);
            Log.d("TAG",this.getDetailsLink());
        }
    }
    public TeacherViewAttendanceItem() {
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

    public String getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(String courseSection) {
        this.courseSection = courseSection;
    }

    public String getCourseCreditHours() {
        return courseCreditHours;
    }

    public void setCourseCreditHours(String courseCreditHours) {
        this.courseCreditHours = courseCreditHours;
    }

    public String getNumberOfLectures() {
        return numberOfLectures;
    }

    public void setNumberOfLectures(String numberOfLectures) {
        this.numberOfLectures = numberOfLectures;
    }

    public String getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(String numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String getDetailsLink() {
        return detailsLink;
    }

    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }

    public boolean searchInCourses(ArrayList<TeacherViewAttendanceItem> attendanceItems, int index) {
        for(int i=0; i<index;i++){
            if(attendanceItems.get(i).getCourseId().equals(this.getCourseId()) && attendanceItems.get(i).getCourseSection().equals(this.getCourseSection())){
                return true;
            }
        }
        return false;
    }
}

package com.providentitgroup.attendergcuf.models;

import com.providentitgroup.attendergcuf.Utility.DataValidator;

import org.jsoup.select.Elements;

import java.util.ArrayList;

public class AssignedCourseItem {
    private String courseId;
    private String courseTitle;
    private String courseCreditHours;
    private String courseClass;

    public AssignedCourseItem(String courseId, String courseTitle, String courseCreditHours, String courseClass) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseCreditHours = courseCreditHours;
        this.courseClass = courseClass;
    }

    public AssignedCourseItem() {
    }
    public AssignedCourseItem(Elements data) throws Exception {
        String [] str =data.get(2).text().trim().split("-");
        this.courseId = str[0]+"-"+str[1];
        this.courseTitle = data.get(2).text().trim();
        this.courseCreditHours = this.courseTitle.substring(this.courseTitle.length()-6).trim();
        this.courseTitle = DataValidator.toTitleCase(this.getCourseTitle().substring(0,this.courseTitle.length()-6).trim());
            this.courseClass = DataValidator.toTitleCase(data.get(1).text().trim());
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

    public String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(String courseClass) {
        this.courseClass = courseClass;
    }

    public boolean searchInCourses(ArrayList<AssignedCourseItem> coursesList, int index) {
        for(int i=0; i<index;i++){
            if(coursesList.get(i).getCourseClass().equals(this.getCourseClass()) && coursesList.get(i).getCourseTitle().equals(this.getCourseTitle())){
                return true;
            }
        }
        return false;
    }
}

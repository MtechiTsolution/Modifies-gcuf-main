package com.providentitgroup.attendergcuf.models;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TimeTableItem {
    private String courseId;
    private String courseTitle;
    private String instructorName;
    private String time;
    private String day;
    private String roomNumber;
    private String building;
    private String program;
    private String semester;
    private String courseCreditHours;

    public TimeTableItem(String courseId, String courseTitle, String instructorName, String time, String day, String roomNumber, String building, String program, String semester, String courseCreditHours) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.instructorName = instructorName;
        this.time = time;
        this.day = day;
        this.roomNumber = roomNumber;
        this.building = building;
        this.program = program;
        this.semester = semester;
        this.courseCreditHours = courseCreditHours;
    }
    public TimeTableItem(Element element) throws Exception {
        this.courseId = element.select(">font").get(0).text().trim();
        try{
                this.courseTitle = element.select(".middle >font").get(1).text().trim();
            if(this.courseTitle.equals("")){
                this.courseTitle=this.courseId;
            }
        }catch (Exception e){
            this.courseTitle=this.courseId;
        }
        try{
            this.courseCreditHours = this.getCourseTitle().substring(this.getCourseTitle().length()-6).trim();
        }catch (Exception e){
            this.courseCreditHours="";
        }
        try{
            if(element.select(">font").size()>2){
                this.instructorName =element.select(">font").get(2).text().trim() ;
            }else{
                this.instructorName =element.select(">font").get(1).text().trim();
            }
        }catch (Exception e){
            this.instructorName ="" ;
        }
        try{
            this.time = element.select(".middle >font").get(0).text().trim();

        }catch (Exception e){
            this.time="";
        }
        try{
            this.day = element.select(".top").text().split("<br>")[0].trim();

        }catch (Exception e){
            this.day="";
        }
        try{
            this.roomNumber = element.select(".bottom >font").get(3).html().trim().split("<br>")[0].trim();

        }catch (Exception e){
            this.roomNumber = "";

        }
        try{
            this.building = element.select(".bottom >font").get(3).html().trim().split("<br>")[1].trim();

        }catch (Exception e){
            this.building = "";

        }
        try{
            this.building = element.select(".bottom >font").get(3).html().trim().split("<br>")[1].trim();

        }catch (Exception e){
            this.building ="";

        }
        try{
            this.program = element.select(">font").get(1).text().trim();

        }
        catch (Exception e){
            this.program = "";

        }
        try{
            this.semester = element.select(".bottom >font").get(0).text().trim();

        }
        catch (Exception e){
            this.semester = "";
        }
    }

    public TimeTableItem() {
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

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseCreditHours() {
        return courseCreditHours;
    }

    public void setCourseCreditHours(String courseCreditHours) {
        this.courseCreditHours = courseCreditHours;
    }
}

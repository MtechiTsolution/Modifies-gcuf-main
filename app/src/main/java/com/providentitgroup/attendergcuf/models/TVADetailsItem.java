package com.providentitgroup.attendergcuf.models;

import android.util.Log;

import com.providentitgroup.attendergcuf.Utility.DataValidator;

import org.jsoup.select.Elements;

public class TVADetailsItem {
    private String studentName;
    private String studentRollNumber;
    private String studentsPercentage;
    private String studentsGender;

    public TVADetailsItem(String studentName, String studentRollNumber, String studentsPercentage, String studentsGender) {
        this.studentName = studentName;
        this.studentRollNumber = studentRollNumber;
        this.studentsPercentage = studentsPercentage;
        this.studentsGender = studentsGender;
    }

    public String getStudentsGender() {
        return studentsGender;
    }

    public void setStudentsGender(String studentsGender) {
        this.studentsGender = studentsGender;
    }

    public TVADetailsItem() {
    }
    public TVADetailsItem(Elements data) throws Exception{
        this.studentName = data.get(2).text().trim();
        this.studentRollNumber = data.get(1).text().trim();;
        this.studentsPercentage = data.get(data.size()-1).text().trim();
        String str =data.get(3).select("img").attr("src");
        if(str.equals("images/male_attendance_icon.gif") || str.equals("images/male_attendance_icon1.gif")){
            this.studentsGender="Male";
        }else{
            this.studentsGender="Female";
        }
    }
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public void setStudentRollNumber(String studentRollNumber) {
        this.studentRollNumber = studentRollNumber;
    }

    public String getStudentsPercentage() {
        return studentsPercentage;
    }

    public void setStudentsPercentage(String studentsPercentage) {
        this.studentsPercentage = studentsPercentage;
    }
}

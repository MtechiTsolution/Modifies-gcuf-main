package com.providentitgroup.attendergcuf.models;

import android.util.Log;

import com.providentitgroup.attendergcuf.Utility.DataValidator;

import org.jsoup.select.Elements;

public class Student {
    private String name;
    private String rollNumber;
    private String secretNumber;
    private int attendance;

    public Student(String name, String rollNumber, String secretNumber, int attendance) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.secretNumber = secretNumber;
        this.attendance = attendance;
    }

    public Student() {
    }
    public Student(Elements data) throws Exception{
       this.name = data.get(2).text().trim();
       this.rollNumber=data.get(1).text().trim();
       this.secretNumber=data.get(5).select("input:nth-of-type(6)").attr("value").trim();
       this.attendance=-1;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getSecretNumber() {
        return secretNumber;
    }

    public void setSecretNumber(String secretNumber) {
        this.secretNumber = secretNumber;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }
}

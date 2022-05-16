package com.providentitgroup.attendergcuf.models;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ResultItem {
    private String courseId;
    private String courseName;
    private String courseCreditHours;
    private String sessionalMarks;
    private String midExamMarks;
    private String finalTheoryMarks;
    private String finalPracticalMarks;
    private String marksTotalSubject;
    private String marksTotalAchieved;
    private String grade;
    private String qualityPoints;
    private String percentage;
    private String attendance;
    private String remarks;
    private static String gpa;
    private static String cgpa;

    public ResultItem(String courseId, String courseName, String courseCreditHours, String sessionalMarks, String midExamMarks, String finalTheoryMarks, String finalPracticalMarks, String marksTotalSubject, String marksTotalAchieved, String grade, String qualityPoints, String percentage, String attendance, String remarks) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCreditHours = courseCreditHours;
        this.sessionalMarks = sessionalMarks;
        this.midExamMarks = midExamMarks;
        this.finalTheoryMarks = finalTheoryMarks;
        this.finalPracticalMarks = finalPracticalMarks;
        this.marksTotalSubject = marksTotalSubject;
        this.marksTotalAchieved = marksTotalAchieved;
        this.grade = grade;
        this.qualityPoints = qualityPoints;
        this.percentage = percentage;
        this.attendance = attendance;
        this.remarks = remarks;
    }

    public ResultItem(Element element) throws Exception{
        Elements cols = element.children();
        courseId = cols.get(1).text().trim().split(" ")[0].trim();
        courseName = cols.get(1).text().trim().substring(courseId.length()+3);
        courseName = courseName.substring(0,courseName.length()-6).trim();
        courseCreditHours = cols.get(1).text().trim().substring(cols.get(1).text().length()-6).trim();
        sessionalMarks = cols.get(2).text().trim();
        midExamMarks = cols.get(3).text().trim();
        finalTheoryMarks = cols.get(4).text().trim();
        finalPracticalMarks = cols.get(5).text().trim();
        marksTotalAchieved = cols.get(6).text().trim();
        marksTotalSubject = cols.get(7).text().trim();
        grade = cols.get(8).text().trim();
        qualityPoints = cols.get(9).text().trim();
        percentage = cols.get(10).text().trim();
        attendance = cols.get(11).text().trim();
        remarks = cols.get(12).text().trim();
        Log.d("TAG",cols.text());
//        this.courseId = " ";
//        this.courseName = "";
//        this.courseCreditHours = " ";
//        this.sessionalMarks = " ";
//        this.midExamMarks = " ";
//        this.finalTheoryMarks = " ";
//        this.finalPracticalMarks = " ";
//        this.marksTotalSubject = " ";
//        this.marksTotalAchieved = " ";
//        this.grade = " ";
//        this.qualityPoints = " ";
//        this.percentage = " ";
//        this.attendance = " ";
//        this.remarks = " ";
    }
    public ResultItem() {
    }

    public static String getGpa() {
        return gpa;
    }

    public static void setGpa(String gpa) {
        ResultItem.gpa = gpa;
    }

    public static String getCgpa() {
        return cgpa;
    }

    public static void setCgpa(String cgpa) {
        ResultItem.cgpa = cgpa;
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

    public String getCourseCreditHours() {
        return courseCreditHours;
    }

    public void setCourseCreditHours(String courseCreditHours) {
        this.courseCreditHours = courseCreditHours;
    }

    public String getSessionalMarks() {
        return sessionalMarks;
    }

    public void setSessionalMarks(String sessionalMarks) {
        this.sessionalMarks = sessionalMarks;
    }

    public String getMidExamMarks() {
        return midExamMarks;
    }

    public void setMidExamMarks(String midExamMarks) {
        this.midExamMarks = midExamMarks;
    }

    public String getFinalTheoryMarks() {
        return finalTheoryMarks;
    }

    public void setFinalTheoryMarks(String finalTheoryMarks) {
        this.finalTheoryMarks = finalTheoryMarks;
    }

    public String getFinalPracticalMarks() {
        return finalPracticalMarks;
    }

    public void setFinalPracticalMarks(String finalPracticalMarks) {
        this.finalPracticalMarks = finalPracticalMarks;
    }

    public String getMarksTotalSubject() {
        return marksTotalSubject;
    }

    public void setMarksTotalSubject(String marksTotalSubject) {
        this.marksTotalSubject = marksTotalSubject;
    }

    public String getMarksTotalAchieved() {
        return marksTotalAchieved;
    }

    public void setMarksTotalAchieved(String marksTotalAchieved) {
        this.marksTotalAchieved = marksTotalAchieved;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getQualityPoints() {
        return qualityPoints;
    }

    public void setQualityPoints(String qualityPoints) {
        this.qualityPoints = qualityPoints;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

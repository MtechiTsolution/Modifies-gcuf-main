package com.providentitgroup.attendergcuf.models;

import com.providentitgroup.attendergcuf.Utility.DataValidator;

import org.jsoup.select.Elements;

public class FeeItem {
    private String entryDate;
    private String voucherNum;
    private String bankName;
    private String semester;
    private String rollNum;
    private String studentName;
    private String fatherName;
    private String programTitle;
    private String feeAmount;
    private String paidAmount;
    private String id;

    public FeeItem(String entryDate, String voucherNum, String bankName, String semester, String rollNum, String studentName, String fatherName, String programTitle, String feeAmount, String paidAmount, String id) {
        this.entryDate = entryDate;
        this.voucherNum = voucherNum;
        this.bankName = bankName;
        this.semester = semester;
        this.rollNum = rollNum;
        this.studentName = studentName;
        this.fatherName = fatherName;
        this.programTitle = programTitle;
        this.feeAmount = feeAmount;
        this.paidAmount = paidAmount;
        this.id = id;
    }

    public FeeItem(Elements data) throws Exception {
        this.entryDate = data.get(1).text().trim();
        this.voucherNum = data.get(2).text().trim();
        this.bankName = data.get(3).text().trim();
        this.semester = data.get(4).text().trim();
        this.rollNum = data.get(5).text().trim();
        this.studentName = DataValidator.toTitleCase(data.get(6).text());
        this.fatherName = DataValidator.toTitleCase(data.get(7).text());
        this.programTitle = data.get(8).text().trim();
        this.feeAmount = data.get(9).text().trim();
        this.paidAmount = data.get(10).text().trim();
        this.id=data.get(0).text().trim();
    }

    public FeeItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(String voucherNum) {
        this.voucherNum = voucherNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getRollNum() {
        return rollNum;
    }

    public void setRollNum(String rollNum) {
        this.rollNum = rollNum;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }
}

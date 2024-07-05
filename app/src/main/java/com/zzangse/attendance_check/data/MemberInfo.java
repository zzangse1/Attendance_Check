package com.zzangse.attendance_check.data;

public class MemberInfo {
    private int priNum;
    private String infoGroupName, infoName, infoNumber, infoNumber2, infoAddress,infoAddress2, infoMemo;
    private String infoCheck, infoDate;

    public MemberInfo() {

    }

    public MemberInfo(String infoCheck) {
        this.infoCheck = infoCheck;

    }

    public MemberInfo(int priNum, String infoName) {
        this.priNum = priNum;
        this.infoName = infoName;
    }

    // 출결 체크 전 생성자
    public MemberInfo(int priNum, String infoName,String infoNumber ) {
        this.priNum = priNum;
        this.infoName = infoName;
        this.infoNumber = infoNumber;
        this.infoCheck = ""; // 출결 체크그 전 기본
    }

    public MemberInfo(int priNum, String infoName,String infoNumber ,String infoCheck) {
        this.priNum = priNum;
        this.infoName = infoName;
        this.infoNumber = infoNumber;
        this.infoCheck = infoCheck;
    }

    public MemberInfo(String infoGroupName, String infoName, String infoNumber, String infoNumber2, String infoAddress,String infoAddress2, String infoMemo) {
        this.infoGroupName = infoGroupName;
        this.infoName = infoName;
        this.infoNumber = infoNumber;
        this.infoNumber2 = infoNumber2;
        this.infoAddress = infoAddress;
        this.infoAddress2 = infoAddress2;
        this.infoMemo = infoMemo;
    }

    public String getInfoCheck() {
        return infoCheck;
    }

    public void setInfoCheck(String infoCheck) {
        this.infoCheck = infoCheck;
    }

    public String getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(String infoDate) {
        this.infoDate = infoDate;
    }

    public int getPriNum() {
        return priNum;
    }

    public void setPriNum(int priNum) {
        this.priNum = priNum;
    }

    public String getInfoGroupName() {
        return infoGroupName;
    }

    public void setInfoGroupName(String infoGroupName) {
        this.infoGroupName = infoGroupName;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getInfoNumber() {
        return infoNumber;
    }

    public void setInfoNumber(String infoNumber) {
        this.infoNumber = infoNumber;
    }

    public String getInfoNumber2() {
        return infoNumber2;
    }

    public void setInfoNumber2(String infoNumber2) {
        this.infoNumber2 = infoNumber2;
    }

    public String getInfoAddress() {
        return infoAddress;
    }

    public void setInfoAddress(String infoAddress) {
        this.infoAddress = infoAddress;
    }

    public String getInfoAddress2() {
        return infoAddress2;
    }

    public void setInfoAddress2(String infoAddress2) {
        this.infoAddress2 = infoAddress2;
    }

    public String getInfoMemo() {
        return infoMemo;
    }

    public void setInfoMemo(String infoMemo) {
        this.infoMemo = infoMemo;
    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "priNum=" + priNum +
                ", infoGroupName='" + infoGroupName + '\'' +
                ", infoName='" + infoName + '\'' +
                ", infoNumber='" + infoNumber + '\'' +
                ", infoNumber2='" + infoNumber2 + '\'' +
                ", infoAddress='" + infoAddress + '\'' +
                ", infoAddress2='" + infoAddress2 + '\'' +
                ", infoMemo='" + infoMemo + '\'' +
                '}';
    }
}

